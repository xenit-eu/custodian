package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.util.Arguments;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

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
     * @param id the gradle plugin id
     * @return {@code true} if the container has a plugin with the specified id {@code id}
     */
    public boolean has(String id) {
        return this.plugins.containsKey(id);
    }


    public Stream<GradlePlugin> stream() {
        return this.plugins.values().stream();
    }

}
