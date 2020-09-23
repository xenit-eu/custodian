package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.ports.spi.buildsystem.DependencyContainer;
import eu.xenit.custodian.ports.spi.buildsystem.DependencyMatcher;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.Getter;

public class GradleDependencyContainer implements DependencyContainer {

    private final Map<String, List<GradleDependency>> map = new LinkedHashMap<>();

    // TODO is this necessary ?
    @Getter
    private final GradleProject project;

    public GradleDependencyContainer(GradleProject project, Stream<GradleDependency> dependencies) {
        Objects.requireNonNull(project, "Argument 'project' is required");
        Objects.requireNonNull(dependencies, "Argument 'dependencies' is required");

        this.project = project;
        this.addAll(dependencies);
    }

    protected void add(GradleDependency dependency) {

        List<GradleDependency> values = this.map
                .computeIfAbsent(dependency.getTargetConfiguration(), k -> new LinkedList<>());
        values.add(dependency);
    }

    protected void addAll(Stream<? extends GradleDependency> stream) {
        stream.forEach(this::add);
    }


    @Override
    public Stream<GradleDependency> stream() {
        return this.map.values().stream().flatMap(Collection::stream);
    }

    @Override
    public DependencyMatcher<GradleDependency> matcher(String notation) {
        return GradleDependencyMatcher.from(null, notation);
    }
}
