package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.util.Arguments;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;

public class GradlePluginContainer {

    public GradlePluginContainer() {

    }

    public GradlePluginContainer(Stream<GradlePlugin> plugins) {
        Arguments.notNull(plugins, "plugins");
        plugins.forEach(plugin -> this.plugins.putIfAbsent(plugin.getId(), plugin));
    }

    private final Map<String, GradlePlugin> plugins = new LinkedHashMap<>();

    /**
     * Check if this container contains a plugin with the specified id.
     *
     * Unqualified plugin ids - like @{code 'java'} - are implicitly converted to
     * qualified plugin ids - like @{code 'org.gradle.java'}.
     *
     * @param id the gradle plugin id
     * @return {@code true} if the container has a plugin with the specified id {@code id}
     */
    public boolean has(String id) {
        return this.plugins.containsKey(toQualifiedPluginId(id));
    }

    public Optional<GradlePlugin> get(String id) {
        return Optional.ofNullable(this.plugins.get(toQualifiedPluginId(id)));
    }

    public Stream<GradlePlugin> stream() {
        return this.plugins.values().stream();
    }



    private static String toQualifiedPluginId(@NonNull String id) {
        if (id.contains(".")) {
            return id;
        }
        return "org.gradle."+id;
    }

}
