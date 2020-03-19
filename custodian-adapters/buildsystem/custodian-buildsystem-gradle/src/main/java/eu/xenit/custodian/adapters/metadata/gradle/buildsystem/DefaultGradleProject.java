package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import java.util.Optional;
import lombok.Getter;

public class DefaultGradleProject implements GradleProject {

    @Getter
    private final String name;

    private final GradleProject parent;

    @Getter
    private final GradleRepositoryContainer repositories = new GradleRepositoryContainer();

    @Getter
    private final GradleDependencyContainer dependencies = new GradleDependencyContainer(this);

    private final GradleProjectContainer childProjects = new GradleProjectContainer();

    public DefaultGradleProject(String name) {
        this(name, null);
    }

    public DefaultGradleProject(String name, GradleProject parent) {
        this.name = name;
        this.parent = parent;

        // setup bi-directional binding
        if (parent != null) {
            parent.getChildProjects().add(this);
        }
    }

    @Override
    public GradleProject getRootProject() {
        return this.getParent().map(GradleProject::getRootProject).orElse(this);
    }

    @Override
    public Optional<GradleProject> getParent() {
        return Optional.ofNullable(this.parent);
    }

    @Override
    public GradleProjectContainer getChildProjects() {
        return this.childProjects;
    }

}
