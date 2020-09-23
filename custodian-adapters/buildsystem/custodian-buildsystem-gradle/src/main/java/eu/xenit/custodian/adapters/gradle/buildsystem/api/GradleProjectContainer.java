package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.ports.spi.buildsystem.ProjectContainer;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
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
