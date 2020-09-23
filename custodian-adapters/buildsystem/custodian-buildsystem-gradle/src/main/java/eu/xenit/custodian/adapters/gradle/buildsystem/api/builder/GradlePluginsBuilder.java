package eu.xenit.custodian.adapters.gradle.buildsystem.api.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePlugin;
import java.util.stream.Stream;

public interface GradlePluginsBuilder {

    GradlePluginsBuilder addPlugin(String id, String version, boolean apply);

    default GradlePluginsBuilder addPlugin(String id) {
        return this.addPlugin(id, null);
    }

    default GradlePluginsBuilder addPlugin(String id, String version) {
        return this.addPlugin(id, version, true);
    }

    Stream<GradlePlugin> build();
}
