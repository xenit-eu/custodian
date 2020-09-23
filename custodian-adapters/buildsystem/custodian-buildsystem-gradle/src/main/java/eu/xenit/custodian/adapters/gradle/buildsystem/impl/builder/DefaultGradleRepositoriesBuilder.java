package eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactRepository;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.MavenRepository;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.MavenRepository.Builder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleRepositoriesBuilder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DefaultGradleRepositoriesBuilder implements GradleRepositoriesBuilder {

    private final Map<String, GradleArtifactRepository> repositories = new LinkedHashMap<>();

    @Override
    public GradleRepositoriesBuilder addMavenRepository(String id, String url) {
        return this.addMavenRepository(id, url, (builder) -> { });
    }

    @Override
    public GradleRepositoriesBuilder addMavenRepository(String id, String url, Consumer<Builder> callback) {
        Builder builder = MavenRepository.withIdAndUrl(id, url);
        callback.accept(builder);

        MavenRepository mavenRepository = builder.build();
        this.repositories.putIfAbsent(mavenRepository.getId(), mavenRepository);

        return this;
    }

    @Override
    public GradleRepositoriesBuilder add(GradleArtifactRepository mavenRepository) {
        Objects.requireNonNull(mavenRepository);
        this.repositories.putIfAbsent(mavenRepository.getId(), mavenRepository);

        return this;
    }

    @Override
    public Stream<GradleArtifactRepository> build() {
        return this.repositories.values().stream();
    }
}
