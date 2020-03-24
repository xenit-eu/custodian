package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.domain.buildsystem.DependencyContainer;
import eu.xenit.custodian.domain.buildsystem.DependencyMatcher;
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

    @Getter
    private final GradleProject project;

    GradleDependencyContainer(GradleProject project) {
        Objects.requireNonNull(project, "Argument 'project' is required");
        this.project = project;
    }

    public void add(GradleDependency dependency) {

        List<GradleDependency> values = this.map
                .computeIfAbsent(dependency.getTargetConfiguration(), k -> new LinkedList<>());
        values.add(dependency);
    }

    public void addAll(Stream<? extends GradleDependency> stream) {
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
