package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.PluginsAssert;
import org.assertj.core.api.AbstractStringAssert;


public class GradlePluginsStringAssert extends AbstractStringAssert<GradlePluginsStringAssert>
        implements PluginsAssert {

    private GradlePluginsStringAssert(String plugins) {
        super(plugins, GradlePluginsStringAssert.class);
    }

    public static GradlePluginsStringAssert from(String buildGradleContent) {
        return new GradlePluginsStringAssert(GradleBuildParser.extractSection("plugins", buildGradleContent));
    }

    @Override
    public GradlePluginsStringAssert hasPlugin(String plugin) {
        return this.contains("id '" + plugin + "'");
    }

    @Override
    public GradlePluginsStringAssert hasPlugin(String plugin, String version) {
        return this.contains("id '" + plugin + "' version '" + version + "'");
    }

    @Override
    public GradlePluginsStringAssert doesNotHavePlugin(String plugin) {
        return this.doesNotContain(plugin);
    }


}
