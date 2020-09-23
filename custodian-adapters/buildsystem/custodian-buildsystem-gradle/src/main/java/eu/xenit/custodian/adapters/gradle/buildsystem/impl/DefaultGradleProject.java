package eu.xenit.custodian.adapters.gradle.buildsystem.impl;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDependencyContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDsl;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePluginContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder.DefaultGradleProjectBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleRepositoryContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProjectContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder.GradleProjectTree;
import eu.xenit.custodian.util.Arguments;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

public class DefaultGradleProject implements GradleProject {

    @Getter
    private final String path;

    @Getter
    private final String name;

    @Getter
    private final GradlePluginContainer plugins;

    @Getter
    private final GradleRepositoryContainer repositories;

    @Getter
    private final GradleDependencyContainer dependencies;

    private final GradleProjectContainer childProjects = new GradleProjectContainer();

    @Getter
    private final GradleDsl dsl;

//    @Getter
    private final String projectDirPath;

    @Getter
    private final String buildFileName;

    private final PathResolver pathResolver;
    private final ProjectResolver projectResolver;

//    private final GradleProjectTree projectTree;

    public DefaultGradleProject(DefaultGradleProjectBuilder builder) {
        Arguments.notNull(builder, "builder");

        this.projectResolver = builder.projectResolver();
        this.pathResolver = builder.rootPathResolver();

        this.dsl = builder.dsl();

        this.path = builder.path();
        this.name = builder.name();
        this.projectDirPath = builder.projectDir();
        this.buildFileName = builder.buildFile();

        this.plugins = new GradlePluginContainer(builder.plugins().build());
        this.repositories = new GradleRepositoryContainer(builder.repositories().build());
        this.dependencies = new GradleDependencyContainer(this, builder.dependencies().build());


    }

    @Override
    public Path getProjectDir() {
        return this.pathResolver.resolve(this.projectDirPath).normalize();
    }

    @Override
    public Path getBuildFile() {
        return this.getProjectDir().resolve(this.buildFileName);
    }

    @Override
    public GradleProject getRootProject() {
        //return this.projectResolver.getRootProject()
        return this.getParent().map(GradleProject::getRootProject).orElse(this);
    }

    @Override
    public Optional<GradleProject> getParent() {
        return this.projectResolver.getParentProject(this);
    }

    @Override
    public GradleProjectContainer getChildProjects() {
        return this.childProjects;
    }

    @Override
    public GradleProjectContainer getAllProjects() {
        GradleProjectContainer result = new GradleProjectContainer();
        result.add(this);

        this.childProjects.stream()
                .flatMap(child -> child.getAllProjects().stream())
                .forEach(result::add);

        return result;
    }
}
