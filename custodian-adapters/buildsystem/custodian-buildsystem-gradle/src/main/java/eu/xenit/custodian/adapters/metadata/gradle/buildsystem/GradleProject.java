package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.ports.spi.build.Project;
import java.util.Collection;
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
}
