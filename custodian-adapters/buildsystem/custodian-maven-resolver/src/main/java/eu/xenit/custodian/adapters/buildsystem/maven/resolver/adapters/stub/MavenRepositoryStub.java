package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.stub;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Stream;

public class MavenRepositoryStub implements ResolverMavenRepository {

    private final Map<String, Map<ResolverArtifactVersion, ResolverArtifact>> data = new HashMap<>();

    private final String id;
    private final String url;

    public MavenRepositoryStub(ResolverMavenRepository repository, Stream<ResolverArtifactSpecification> artifacts) {
        this(repository.getId(), repository.getUrl(), artifacts);
    }

    public MavenRepositoryStub(String id, String url, Stream<ResolverArtifactSpecification> artifacts) {
        Objects.requireNonNull(artifacts, "Argument 'artifacts' is required");

        this.id = id;
        this.url = url;

        artifacts.forEach(spec -> {
            String key = spec.getGroupId() + ":" + spec.getArtifactId();
            this.data.computeIfAbsent(key, k -> new HashMap<>()).put(
                    ResolverArtifactVersion.from(spec.getVersion()),
                    ResolverArtifact.from(spec.getClassifier(), spec.getExtension())
            );
        });
    }

    public Stream<ResolverArtifactVersion> resolveVersionRange(ResolverArtifactSpecification request) {
        String id = request.getGroupId() + ":" + request.getArtifactId();
        var map = this.data.getOrDefault(id, Collections.emptyMap());

        // looping over all artifacts-versions registered with this groupId:artifactId
        // add to the result if version/classifier/extension matches the request
        return map.entrySet().stream()
                // filter on version spec
                .filter(pair -> {
                    var versionSpec = ResolverVersionSpecification.from(request.getVersion());
                    return versionSpec.matches(pair.getKey());
                })

                // filter on classifier
                .filter(pair -> Objects.equals(pair.getValue().getClassifier(), request.getClassifier()))

                // filter on extension
                .filter(pair -> Objects.equals(pair.getValue().getExtension(), request.getExtension()))

                // map to the version number
                .map(Entry::getKey)

                // sorted ascending order, using natural ordering
                .sorted();
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}
