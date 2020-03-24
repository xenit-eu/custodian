package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.ports.spi.build.Project;
import java.nio.file.Path;
import java.util.Optional;

public interface GradleProject extends Project {

    @Override
    GradleProject getRootProject();

    @Override
    GradleProjectContainer getChildProjects();

    @Override
    Optional<GradleProject> getParent();

    GradleDependencyContainer getDependencies();
    GradleRepositoryContainer getRepositories();

    Path getProjectDir();
    Path getBuildFile();
}
