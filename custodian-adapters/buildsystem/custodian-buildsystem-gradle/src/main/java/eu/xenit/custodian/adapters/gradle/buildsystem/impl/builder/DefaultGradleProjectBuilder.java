package eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDsl;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleDependenciesBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradlePluginsBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleProjectBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleRepositoriesBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.PathResolver;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.ProjectResolver;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class DefaultGradleProjectBuilder implements GradleProjectBuilder {

    private final ProjectResolver projectResolver;
    private final PathResolver rootPathResolver;

    private GradleDsl dsl;
    private String path = ":";

    private String name;
    private GradleProject parent;

    private String projectDir = ".";
    private String buildFile = "build.gradle";

    private final GradlePluginsBuilder plugins = new DefaultGradlePluginsBuilder();
    private final GradleRepositoriesBuilder repositories = new DefaultGradleRepositoriesBuilder();
    private final GradleDependenciesBuilder dependencies = new DefaultGradleDependenciesBuilder();

    DefaultGradleProjectBuilder(ProjectResolver projectResolver, PathResolver rootPathResolver, GradleDsl dsl) {
        this.projectResolver = Objects.requireNonNull(projectResolver);
        this.rootPathResolver = Objects.requireNonNull(rootPathResolver);
        this.dsl = Objects.requireNonNull(dsl);
    }

    DefaultGradleProject build() {
        return new DefaultGradleProject(this);
    }

    @Override
    public GradleProjectBuilder withPlugins(Consumer<GradlePluginsBuilder> callback) {
        callback.accept(this.plugins);
        return this;
    }

    @Override
    public GradleProjectBuilder withRepositories(Consumer<GradleRepositoriesBuilder> callback) {
        callback.accept(this.repositories);
        return this;
    }

    @Override
    public GradleProjectBuilder withDependencies(Consumer<GradleDependenciesBuilder> callback) {
        callback.accept(this.dependencies);
        return this;
    }
}
