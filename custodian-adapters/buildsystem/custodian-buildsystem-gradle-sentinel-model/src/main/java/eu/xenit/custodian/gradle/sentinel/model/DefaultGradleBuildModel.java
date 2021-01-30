package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class DefaultGradleBuildModel implements GradleBuildModel, Serializable {

    private final Map<String, ProjectModel> projects;

    public DefaultGradleBuildModel(Map<String, ProjectModel> projects) {
        if (projects == null || projects.isEmpty()) {
            throw new IllegalArgumentException("projects is required");
        }

        // check that it does not contain null-values
        projects.values().forEach(project -> Objects.requireNonNull(project, "project model can't be null"));

        // check that there is a project with path ':"
        if (!projects.containsKey(":")) {
            throw new IllegalArgumentException("Expected projects to contain a root-project with path ':'");
        }

        this.projects = projects;
    }

    @Override
    public String getRootDirectory() {
        var root = this.getRootProject();
//        return Paths.get(root.getProjectDir()).toAbsolutePath();
        return root.getProjectDir();
    }

    /**
     * Get the root project.
     *
     * @return the root project - never null.
     */
    @Override
    public ProjectModel getRootProject() {
        return this.projects.get(":");
    }

    @Override
    public Collection<ProjectModel> getAllProjects() {
//        return ImmutableDomainObjectSet.of(this.projects.values());
        return this.projects.values();
    }

}
