package eu.xenit.custodian.adapters.gradle.buildsystem.spi.sentinel.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Dependencies {

    private Map<String, DependencySet> map = new LinkedHashMap<>();

    public Dependencies(Map<String, DependencySet> dependencies) {
        dependencies.forEach((configuration, dependencySet) -> map.put(configuration, dependencySet));
    }

    public Dependencies(Stream<Entry<String, DependencySet>> stream) {
        stream.forEach(entry -> map.put(entry.getKey(), entry.getValue()));
    }

    @JsonAnySetter
    public void addDependencySet(String name, DependencySet dependencySet) {
        Objects.requireNonNull(name, "Argument 'name' is required");
        Objects.requireNonNull(dependencySet, "Argument 'dependencySet' is required");

        this.map.put(name, dependencySet);
    }

    public DependencySet get(String configuration) {
        return this.map.get(configuration);
    }

    public Stream<Map.Entry<String, DependencySet>> stream() {
        return this.map.entrySet().stream();
    }
}
