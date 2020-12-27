package eu.xenit.custodian.adapters.gradle.buildsystem.asserts;

public interface PluginsAssert {

    PluginsAssert hasPlugin(String plugin);

    PluginsAssert hasPlugin(String plugin, String version);

    PluginsAssert doesNotHavePlugin(String plugin);

    default PluginsAssert hasJavaPlugin() {
        return this.hasPlugin("org.gradle.java");
    }
}
