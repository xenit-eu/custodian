package eu.xenit.custodian.adapters.gradle.buildsystem.api.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDsl;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.MavenRepository;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface GradleProjectBuilder {

    GradleProjectBuilder name(String name);
    GradleProjectBuilder group(String group);
    GradleProjectBuilder version(String version);

    GradleProjectBuilder path(String path);

    GradleProjectBuilder dsl(GradleDsl dsl);

    GradleProjectBuilder projectDir(String projectDir);
    GradleProjectBuilder buildFile(String buildFile);

    GradleProjectBuilder withPlugins(Consumer<GradlePluginsBuilder> callback);
    GradleProjectBuilder withRepositories(Consumer<GradleRepositoriesBuilder> callback);
    GradleProjectBuilder withDependencies(Consumer<GradleDependenciesBuilder> callback);

    default GradleProjectBuilder withMavenCentral() {
        return withRepositories(repositories -> repositories.add(MavenRepository.mavenCentral()));
    }

    default GradleProjectBuilder withJavaPlugin() {
        return withPlugins(plugins -> plugins.addPlugin("java"));
    }
}
