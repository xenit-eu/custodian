package eu.xenit.custodian.sentinel.adapters.repositories;

import eu.xenit.custodian.sentinel.domain.JsonPartialReporter;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import eu.xenit.custodian.sentinel.reporting.io.JsonWriter;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class JsonRepositoriesReporter implements JsonPartialReporter<RepositoriesContainer> {

    @Override
    public Runnable report(IndentingWriter writer, RepositoriesContainer repositories) {
        JsonWriter json = new JsonWriter(writer);
        return reportRepositories(json, repositories);
    }

    private Runnable reportRepositories(JsonWriter json, RepositoriesContainer repositories) {
        return json.array(repositories.items(), this.writeRepositoryCallback(json));
    }

    private Function<RepositoryResult, Runnable> writeRepositoryCallback(JsonWriter json) {
        return (repository) -> json.object(
                Optional.of(json.property("name", repository.getName())),
                Optional.of(json.property("type", repository.getType())),
                Optional.of(json.property("url", json.value(repository.getUrl()))),
                Optional.of(json.property("metadataSources", json.array(repository.getMetadataSources(), json::value))),
                json.property("ivy", writeIvyRepositoryDetails(json, repository))
        );
    }

    private Optional<Runnable> writeIvyRepositoryDetails(JsonWriter json, RepositoryResult repository) {
        if (repository.getIvy() == null) {
            return Optional.empty();
        }

        return Optional.of(json.object(
                json.property("layout", repository.getIvy().getLayoutType()),
                json.property("m2compatible", Boolean.toString(repository.getIvy().isM2Compatible())),
                json.property("artifactPatterns", json.array(repository.getIvy().getArtifactPatterns(), json::value)),
                json.property("ivyPatterns", json.array(repository.getIvy().getIvyPatterns(), json::value))
        ));
    }

//        writer.writeProperty("repositories", array(writer, repositories, this::writeRepository));
//    }
//
//    private void writeRepository(JsonWriter writer, RepositoryResult repository) {
//        writer.writeObject(
//                property(writer, "name", repository.getName()),
//                property(writer, "type", repository.getType()),
//                property(writer, "url", repository.getUrl()),
//                property(writer, "metadataSources", array(writer, repository.getMetadataSources())),
//                property(writer, "ivy", () -> writer.writeObject(
//                        property(writer, "layout", repository.getIvy().getLayoutType()),
//                        property(writer, "m2compatible", repository.getIvy().isM2Compatible()),
//                        property(writer, "artifactPatterns", array(writer, repository.getIvy().getArtifactPatterns())),
//                        property(writer, "ivyPatterns", array(writer, repository.getIvy().getIvyPatterns()))
//                ), () -> repository.getIvy() != null)
//        );
//    }
}
