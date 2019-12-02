package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.adapters.metadata.gradle.notation.GradleStringNotationParser;
import eu.xenit.custodian.domain.buildsystem.DependencyContainer;
import eu.xenit.custodian.domain.buildsystem.DependencyMatcher;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

//public class GradleDependencyContainer extends DependencyContainer<ExternalModuleIdentifier, DefaultBackupGradleModuleDependency> {
public class GradleDependencyContainer implements DependencyContainer<GradleDependency> {

    private final Map<String, List<GradleDependency>> map = new LinkedHashMap<>();

    GradleDependencyContainer() {

    }

    public void add(GradleDependency dependency) {

        List<GradleDependency> values = this.map
                .computeIfAbsent(dependency.getTargetConfiguration(), k -> new LinkedList<>());
        values.add(dependency);
    }

    public void add(Stream<? extends GradleDependency> stream) {
        stream.forEach(this::add);
    }

    public Stream<GradleDependency> matches(DependencyMatcher<GradleDependency> matcher) {
        return this.stream().filter(matcher);
    }


    @Override
    public DependencyMatcher<GradleDependency> matcher(String notation) {
        return GradleDependencyMatcher.from(notation);
    }

    @Override
    public Stream<GradleDependency> stream() {
        return this.map.values().stream().flatMap(Collection::stream);
    }
}
