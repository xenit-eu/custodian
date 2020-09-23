package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import org.assertj.core.api.AbstractStringAssert;


public class PluginAssert extends AbstractStringAssert<PluginAssert> {

    private PluginAssert(String plugins) {
        super(plugins, PluginAssert.class);
    }

    public static PluginAssert from(String buildGradleContent) {
        return new PluginAssert(GradleBuildParser.extractSection("plugins", buildGradleContent));
    }

    public PluginAssert hasPlugin(String plugin) {
        return this.contains("id '" + plugin + "'");
    }

    public PluginAssert hasPlugin(String plugin, String version) {
        return this.contains("id '" + plugin + "' version '" + version + "'");
    }

    public PluginAssert doesNotHavePlugin(String plugin) {
        return this.doesNotContain(plugin);
    }




}
