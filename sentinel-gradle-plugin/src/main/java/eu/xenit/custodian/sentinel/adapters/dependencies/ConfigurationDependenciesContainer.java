package eu.xenit.custodian.sentinel.adapters.dependencies;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

public class ConfigurationDependenciesContainer {

    private final List<DeclaredModuleDependency> dependencies;

    @Getter
    private final String name;

    public ConfigurationDependenciesContainer(String configurationName) {
        this(configurationName, Stream.empty());
    }

    public ConfigurationDependenciesContainer(String configurationName, Stream<DeclaredModuleDependency> dependencies) {
        this.name = configurationName;
        this.dependencies = dependencies.collect(Collectors.toList());
    }

    public Stream<DeclaredModuleDependency> dependencies() {
        return this.dependencies.stream();
    }

    public boolean isEmpty() {
        return this.dependencies.isEmpty();
    }

    public void add(DeclaredModuleDependency dependency) {
        Objects.requireNonNull(dependency, "Argument 'dependency' is required");
        this.dependencies.add(dependency);
    }

}
