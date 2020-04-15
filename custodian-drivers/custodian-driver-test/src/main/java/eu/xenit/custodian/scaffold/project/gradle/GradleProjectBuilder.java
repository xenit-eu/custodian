package eu.xenit.custodian.scaffold.project.gradle;

import io.spring.initializr.generator.buildsystem.MavenRepository;
import io.spring.initializr.generator.buildsystem.MavenRepositoryContainer;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradlePluginContainer;
import java.util.function.Consumer;

public class GradleProjectBuilder {

    private final GradleBuild gradleBuild;

    private GradleProjectBuilder(GradleBuild gradleBuild) {
        this.gradleBuild = gradleBuild;
    }

    public static GradleProjectBuilder create() {
        return new GradleProjectBuilder(new GradleBuild());
    }

    public GradleProject build() {
        return new GradleProject(this.gradleBuild);
    }

    public GradleProjectBuilder withJavaPlugin() {
        return this.withPlugins(plugins -> plugins.apply("java"));
    }

    public GradleProjectBuilder withRepositories(Consumer<MavenRepositoryContainer> callback) {
        callback.accept(this.gradleBuild.repositories());
        return this;
    }

    public GradleProjectBuilder withMavenCentral() {
        return this.withRepositories(repositories -> {
            repositories.add(MavenRepository.MAVEN_CENTRAL);
        });
    }

    public GradleProjectBuilder withPlugins(Consumer<GradlePluginContainer> plugins)
    {
        plugins.accept(this.gradleBuild.plugins());
        return this;
    }

    public GradleProjectBuilder withDependencies(Consumer<DependencyContainerHandler> callback) {
        callback.accept(new DependencyContainerHandler(this.gradleBuild.dependencies()));
        return this;
    }

}
