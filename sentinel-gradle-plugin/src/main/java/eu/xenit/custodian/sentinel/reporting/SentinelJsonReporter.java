package eu.xenit.custodian.sentinel.reporting;

import eu.xenit.custodian.sentinel.analyzer.model.DependencyResolution;
import eu.xenit.custodian.sentinel.analyzer.model.DependencyResolution.DependencyResolutionState;
import eu.xenit.custodian.sentinel.analyzer.model.SentinelAnalysisResult;
import eu.xenit.custodian.sentinel.analyzer.model.AnalyzedDependency;
import eu.xenit.custodian.sentinel.analyzer.model.ConfigurationResult;
import eu.xenit.custodian.sentinel.analyzer.model.RepositoryResult;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import eu.xenit.custodian.sentinel.reporting.io.JsonWriter;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.gradle.api.artifacts.result.ComponentSelectionDescriptor;
import org.gradle.api.artifacts.result.ComponentSelectionReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentinelJsonReporter implements SentinelReporter {


     private Logger log = LoggerFactory.getLogger(SentinelJsonReporter.class);

    @Override
    public void write(IndentingWriter writer, SentinelAnalysisResult result) {
        this.write(new JsonWriter(writer), result);
    }

    private void write(JsonWriter writer, SentinelAnalysisResult result) {
        writer.writeObject(
                () -> this.writeRepositories(writer, result.getRepositories().items()),
                () -> this.writeConfigurations(writer, result.getConfigurations().items())
        );
        writer.println();
    }

    private void writeRepositories(JsonWriter writer, Collection<RepositoryResult> repositories) {
        writer.writeProperty("repositories", array(writer, repositories, this::writeRepository));
    }

    private void writeRepository(JsonWriter writer, RepositoryResult repository) {
        writer.writeObject(
                property(writer, "name", repository.getName()),
                property(writer, "type", repository.getType()),
                property(writer, "url", repository.getUrl()),
                property(writer, "metadataSources", array(writer, repository.getMetadataSources())),
                property(writer, "ivy", () -> writer.writeObject(
                        property(writer, "layout", repository.getIvy().getLayoutType()),
                        property(writer, "m2compatible", repository.getIvy().isM2Compatible()),
                        property(writer, "artifactPatterns", array(writer, repository.getIvy().getArtifactPatterns())),
                        property(writer, "ivyPatterns", array(writer, repository.getIvy().getIvyPatterns()))
                ), () -> repository.getIvy() != null)
        );
    }

    private void writeConfigurations(JsonWriter writer, Collection<ConfigurationResult> configurations) {
        writer.writeProperty("configurations", () -> writer.writeObject(
                configurations.stream()
                        // Filter out configurations without dependencies ?
                        .filter(configuration -> !configuration.getDependencies().isEmpty())
                        .map(configuration -> property(writer, configuration.getName(),
                                () -> writeConfiguration(writer, configuration)))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
        ));

    }

    private void writeConfiguration(JsonWriter writer, ConfigurationResult configuration) {
        writer.writeObject(
                // disabled for now
                // this.property(writer, "extendsFrom", this.array(writer, configuration.getExtendsFrom())),
                this.property(writer, "dependencies",
                        this.array(writer, configuration.getDependencies().items(), this::writeDependency)
                )
        );
    }

    private void writeDependency(JsonWriter writer, AnalyzedDependency dependency) {
        writer.writeObject(
            property(writer, "group", dependency.getGroup()),
            property(writer, "artifact", dependency.getName()),
            property(writer, "version", dependency.getVersion()),

            property(writer, "resolution", () -> writeDependencyResolution(writer, dependency.getResolution()), () -> dependency.getResolution() != null)

        );
    }

    private void writeDependencyResolution(JsonWriter writer, DependencyResolution resolution) {


        if (resolution.getState() == DependencyResolutionState.RESOLVED) {
            writer.writeObject(
                    property(writer, "state", resolution.getState().toString()),
                    property(writer, "selected", resolution.getSelected().toString()),
                    property(writer, "repository", resolution.getRepository()),
                    property(writer, "reason", writeDependencyResolutionReasons(writer, resolution.getReason()))
            );
        } else {
            writer.writeObject(
                    property(writer, "state", resolution.getState().toString()),
                    property(writer, "failure", resolution.getFailure())
            );

        }
    }

    private Runnable writeDependencyResolutionReasons(JsonWriter writer, ComponentSelectionReason reason) {
        return this.array(writer, reason.getDescriptions().stream()
                .map(ComponentSelectionDescriptor::getDescription)
                .collect(Collectors.toList()));
    }


    /* should we move everything below into JsonWriter ? */

    private Optional<Runnable> property(JsonWriter writer, String name, Object value) {
        return this.property(writer, name, () -> writer.print(quoted(value != null ? value.toString() : "")));
    }

    private Optional<Runnable> property(JsonWriter writer, String name, String value) {
        return this.property(writer, name, () -> writer.print(quoted(value)));
    }

    private Optional<Runnable> property(JsonWriter writer, String name, Runnable value) {
//        return Optional.of(() -> writer.writeProperty(name, value));
        return this.property(writer, name, value, () -> {
            if (value == null) {
                log.warn("Trying to write property {}, but value is unexpectedly 'null'", name);
                return false;
            }

            return true;
        });
    }

    private Optional<Runnable> property(JsonWriter writer, String name, Runnable value, BooleanSupplier conditional) {
        if (conditional.getAsBoolean())
            return Optional.of(() -> writer.writeProperty(name, value));

        return Optional.empty();
    }

    private Runnable array(JsonWriter writer, Collection<String> collection) {
        return this.array(writer, collection, (w, element) -> w.print(quoted(element)));
    }

    private <T> Runnable array(JsonWriter writer, Collection<T> collection, BiConsumer<JsonWriter, T> callback) {
        return () -> {
            writer.writeArray(collection.stream()
                    .map(element -> (Runnable) () -> callback.accept(writer, element))
                    .toArray(Runnable[]::new));
        };
    }

    private static String quoted(@Nullable String str) {
        return (str != null ? "\"" + str + "\"" : "\"\"");
    }

}
