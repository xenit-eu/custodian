package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePlugin;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePluginContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.PluginsAssert;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;

public class GradleModelPluginsAssert
        extends AbstractAssert<GradleModelPluginsAssert, GradlePluginContainer>
        implements PluginsAssert {

    public GradleModelPluginsAssert(GradlePluginContainer actual) {
        super(actual, GradleModelPluginsAssert.class);
    }

    public GradleModelPluginsAssert hasPlugin(String plugin) {
        this.isNotNull();

        if (!this.actual.has(plugin)) {
            this.failWithMessage("Plugin <%s> not found in %s", plugin,
                    this.actual.stream().map(GradlePlugin::toString).collect(Collectors.toList()));
        }

        this.actual.has(plugin);
        return this.myself;
    }

    @Override
    public PluginsAssert hasPlugin(String id, String version) {
        Objects.requireNonNull(id, "Argument 'id' is required");

        this.isNotNull();

        this.actual.get(id).ifPresentOrElse(plugin ->
                {
                    if (!Objects.equals(version, plugin.getVersion())) {
                        var msg = "Plugin %s version mismatch, got <%s> but expected <%s>";
                        this.failWithMessage(msg, id, plugin.getVersion(), version);
                    }
                },
                () -> {
                    var plugins = this.actual.stream().map(GradlePlugin::toString).collect(Collectors.toList());
                    this.failWithMessage("Plugin <%s> not found in %s", id, plugins);
                }
        );
        return this.myself;
    }

    @Override
    public PluginsAssert doesNotHavePlugin(String plugin) {
        return null;
    }
}
