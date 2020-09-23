package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.ports.spi.buildsystem.Project;
import java.nio.file.Path;
import java.util.Optional;

public interface GradleProject extends Project {

    String getPath();

    GradleDsl getDsl();

    @Override
    GradleProject getRootProject();

    @Override
    GradleProjectContainer getChildProjects();

    @Override
    GradleProjectContainer getAllProjects();

    @Override
    Optional<GradleProject> getParent();

    GradleDependencyContainer getDependencies();

    // TODO fix container type
    GradleRepositoryContainer getRepositories();

    Path getProjectDir();
    Path getBuildFile();

    GradlePluginContainer getPlugins();
}
