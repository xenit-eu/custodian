package eu.xenit.custodian.adapters.gradle.buildsystem.api.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactRepository;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.MavenRepository;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.MavenRepository.Builder;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface GradleRepositoriesBuilder {

    GradleRepositoriesBuilder addMavenRepository(String id, String url);
    GradleRepositoriesBuilder addMavenRepository(String id, String url, Consumer<Builder> callback);

    GradleRepositoriesBuilder add(GradleArtifactRepository mavenRepository);

    Stream<GradleArtifactRepository> build();
}
