package eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePlugin;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradlePluginsBuilder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultGradlePluginsBuilder implements GradlePluginsBuilder {

    private Map<String, GradlePlugin> plugins = new LinkedHashMap<>();

    @Override
    public GradlePluginsBuilder addPlugin(String id, String version, boolean apply) {
        GradlePlugin plugin = from(id, version, apply);
        this.plugins.put(id, plugin);
        return this;
    }

    @Override
    public Stream<GradlePlugin> build() {
        return this.plugins.values().stream();
    }

    private static GradlePlugin from(String id, String version, boolean apply) {
        return new GradlePlugin(id, version, apply);
    }
}
