package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePlugin;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePluginContainer;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;

public class GradlePluginsAssert extends AbstractAssert<GradlePluginsAssert, GradlePluginContainer> {

    public GradlePluginsAssert(GradlePluginContainer actual) {
        super(actual, GradlePluginsAssert.class);
    }

    public GradlePluginsAssert hasPlugin(String plugin) {
        this.isNotNull();

        if (!this.actual.has(plugin))
        {
            this.failWithMessage("Plugin <%s> not found in %s", plugin,
                    this.actual.stream().map(GradlePlugin::toString).collect(Collectors.toList()));
        }

        this.actual.has(plugin);
        return this.myself;
    }
}
