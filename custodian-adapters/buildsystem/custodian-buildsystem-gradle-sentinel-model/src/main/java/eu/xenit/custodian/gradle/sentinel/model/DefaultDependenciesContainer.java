package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DefaultDependenciesContainer implements DependenciesContainer, Serializable {

    private final Map<String, Set<DependencyModel>> dependenciesByConfiguration = new LinkedHashMap<>();

    public void addExternalModule(String configuration, String group, String name, String version) {
        if (configuration == null || configuration.trim().isEmpty()) {
            throw new IllegalArgumentException("configuration is required");
        }

        final Set<DependencyModel> dependencies = this.dependenciesByConfiguration
                .computeIfAbsent(configuration, key -> new LinkedHashSet<>());

        dependencies.add(new DefaultDependencyModel(group, name, version));
    }

    public Map<String, Set<DependencyModel>> all() {
        return Collections.unmodifiableMap(this.dependenciesByConfiguration);
    }
}
