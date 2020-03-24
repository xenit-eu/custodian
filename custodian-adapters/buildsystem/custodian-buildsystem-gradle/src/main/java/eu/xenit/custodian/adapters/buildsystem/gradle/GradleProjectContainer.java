package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.domain.buildsystem.ProjectContainer;
import eu.xenit.custodian.ports.spi.build.Project;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class GradleProjectContainer implements ProjectContainer {

    private final Map<String, GradleProject> projects = new LinkedHashMap<>();

    @Override
    public Stream<GradleProject> stream() {
        return this.projects.values().stream();
    }

    @Override
    public Iterator<? extends Project> iterator() {
        return this.projects.values().iterator();
    }

    @Override
    public Optional<GradleProject> getProject(String name) {
        return Optional.ofNullable(projects.get(name));
    }

    public void add(GradleProject project) {
        this.projects.put(project.getName(), project);
    }
}
