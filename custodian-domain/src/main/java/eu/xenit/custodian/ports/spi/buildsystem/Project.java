package eu.xenit.custodian.ports.spi.buildsystem;

import java.util.Optional;

public interface Project {

    String getName();
    String getVersion();

    DependencyContainer getDependencies();

    /**
     * Returns the root project for the hierarchy that this project belongs to.
     *
     * @return The root project. Never returns null.
     */
    Project getRootProject();

    Optional<? extends Project> getParent();

    /**
     * Returns the direct children of this project.
     *
     * @return A container object with direct child projects.
     */
    ProjectContainer getChildProjects();

    /**
     * Returns a list of this project and all child-projects recursively.
     *
     * @return A container object with all projects under this project tree
     */
    ProjectContainer getAllProjects();



}
