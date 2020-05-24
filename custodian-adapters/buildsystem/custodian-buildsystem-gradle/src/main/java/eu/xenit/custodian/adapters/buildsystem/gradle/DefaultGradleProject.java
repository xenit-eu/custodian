package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.util.Arguments;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

public class DefaultGradleProject implements GradleProject {

    @Getter
    private final String name;

    private final GradleProject parent;

    @Getter
    private final GradleRepositoryContainer repositories = new GradleRepositoryContainer();

    @Getter
    private final GradleDependencyContainer dependencies = new GradleDependencyContainer(this);

    private final GradleProjectContainer childProjects = new GradleProjectContainer();

    @Getter
    private final Path projectDir;

    @Getter @Setter
    private Path buildFile;

    public DefaultGradleProject(String name) {
        this(Paths.get("."), name, null);
    }

    public DefaultGradleProject(Path projectDir, String name, GradleProject parent) {
        Arguments.notNull(projectDir, "projectDir");

        this.projectDir = projectDir;
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
