package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.JsonPartialReporter;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import eu.xenit.custodian.sentinel.reporting.io.JsonWriter;

public class JsonDependenciesReporter implements JsonPartialReporter<DependenciesContainer> {

    @Override
    public Runnable report(IndentingWriter writer, DependenciesContainer configurations) {
        JsonWriter json = new JsonWriter(writer);
        return this.report(json, configurations);
    }

    public Runnable report(JsonWriter json, DependenciesContainer dependencies) {
        return json.object(dependencies.items()

                // Filter out configurations without dependencies ?
                .filter(configuration -> !configuration.isEmpty())

                .map(configuration -> json
                        .property(configuration.getName(), this.writeDependenciesFor(json, configuration))
                )
        );
    }

    private Runnable writeDependenciesFor(JsonWriter json, ConfigurationDependenciesContainer configuration) {
        return json.array(configuration.dependencies(), this::writeDependency);

    }

    private Runnable writeDependency(JsonWriter json, DeclaredModuleDependency dependency) {
        return json.object(
                json.property("group", dependency.getGroup()),
                json.property("artifact", dependency.getName()),
                json.property("version", dependency.getVersion())
        );
    }

//    private Function<AnalyzedDependency, Runnable> dependencyWriter(JsonWriter json) {
//        return (dependency) -> json.object(
//                Stream.of(
//                        Optional.of(json.property("group", dependency.getGroup())),
//                        Optional.of(json.property("artifact", dependency.getName())),
//                        Optional.of(json.property("version", dependency.getVersion())),
//                        json.property("resolution", this.writeDependencyResolution(json, dependency.getResolution()))
//                ).filter(Optional::isPresent).map(Optional::get)
//        );
//    }
//
//    private Optional<Runnable> writeDependencyResolution(JsonWriter json, DependencyResolution resolution) {
//        if (resolution == null) {
//            return Optional.empty();
//        }
//
//        if (resolution.getState() == DependencyResolutionState.RESOLVED) {
//            return Optional.of(json.object(
//                    json.property("state", resolution.getState().toString()),
//                    json.property("selected", resolution.getSelected().toString()),
//                    json.property("repository", resolution.getRepository()),
//                    json.property("reason", json.array(resolution.getReason().getDescriptions(), json::value))
//            ));
//        } else {
//            return Optional.of(json.object(
//                    json.property("state", resolution.getState().toString()),
//                    json.property("failure", resolution.getFailure())
//            ));
//        }
//
//    }
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
