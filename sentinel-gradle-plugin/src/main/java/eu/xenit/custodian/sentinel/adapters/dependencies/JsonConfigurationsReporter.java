package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.JsonPartialReporter;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import eu.xenit.custodian.sentinel.reporting.io.JsonWriter;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class JsonConfigurationsReporter implements JsonPartialReporter<ConfigurationContainer> {

    @Override
    public Runnable report(IndentingWriter writer, ConfigurationContainer configurations) {
        JsonWriter json = new JsonWriter(writer);
        return this.reportConfigurations(json, configurations);
    }

    public Runnable reportConfigurations(JsonWriter json, ConfigurationContainer configurations) {
        return json.object(
                configurations.items()
                        // Filter out configurations without dependencies ?
                        .filter(configuration -> !configuration.getDependencies().isEmpty())
                        .map(configuration -> json
                                .property(configuration.getName(), this.writeConfiguration(json, configuration)))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
//        ));
        );
    }

    private Optional<Runnable> writeConfiguration(JsonWriter json, ConfigurationResult configuration) {
        Runnable result = json.object(
                // disabled for now
                // this.property(writer, "extendsFrom", this.array(writer, configuration.getExtendsFrom())),
                json.property("dependencies",
                        json.array(configuration.getDependencies().items(), this.dependencyWriter(json))
                )
        );
        return Optional.of(result);
    }

    private Function<AnalyzedDependency, Runnable> dependencyWriter(JsonWriter json) {
        return (dependency) -> json.object(
                Stream.of(
                        Optional.of(json.property("group", dependency.getGroup())),
                        Optional.of(json.property("artifact", dependency.getName())),
                        Optional.of(json.property("version", dependency.getVersion())),
                        json.property("resolution", this.writeDependencyResolution(json, dependency.getResolution()))
                )
                        .filter(Optional::isPresent)
                        .map(Optional::get)
        );
//        return (dependency) -> {
//            return json.property("foo", dependency.getName());
//        };
    }

    private Optional<Runnable> writeDependencyResolution(JsonWriter json, DependencyResolution resolution) {

        return Optional.empty();
    }
//
//    }
//
//    private void writeDependencyResolution(JsonWriter writer, DependencyResolution resolution) {
//
//        if (resolution.getState() == DependencyResolutionState.RESOLVED) {
//            writer.writeObject(
//                    property(writer, "state", resolution.getState().toString()),
//                    property(writer, "selected", resolution.getSelected().toString()),
//                    property(writer, "repository", resolution.getRepository()),
//                    property(writer, "reason", writeDependencyResolutionReasons(writer, resolution.getReason()))
//            );
//        } else {
//            writer.writeObject(
//                    property(writer, "state", resolution.getState().toString()),
//                    property(writer, "failure", resolution.getFailure())
//            );
//
//        }
//    }
//
//    private Runnable writeDependencyResolutionReasons(JsonWriter writer, ComponentSelectionReason reason) {
//        return this.array(writer, reason.getDescriptions().stream()
//                .map(ComponentSelectionDescriptor::getDescription)
//                .collect(Collectors.toList()));
//    }
}
