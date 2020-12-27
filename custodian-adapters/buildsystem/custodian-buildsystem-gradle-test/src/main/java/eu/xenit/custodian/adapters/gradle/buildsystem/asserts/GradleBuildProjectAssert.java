package eu.xenit.custodian.adapters.gradle.buildsystem.asserts;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.DependenciesAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleBuildFileAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleRepositoriesAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleProjectAssert;
import java.util.function.Consumer;

public interface GradleBuildProjectAssert<ASSERT> {

    ASSERT assertPlugins(Consumer<PluginsAssert> plugins);

    default ASSERT hasPlugin(String pluginId) {
        return this.assertPlugins(plugins -> plugins.hasPlugin(pluginId));
    }

    default ASSERT hasPlugin(String plugin, String version) {
        return this.assertPlugins(plugins -> plugins.hasPlugin(plugin, version));
    }

    default ASSERT hasJavaPlugin() {
        return this.assertPlugins(PluginsAssert::hasJavaPlugin);
    }

    default ASSERT doesNotHavePlugin(String plugin) {
        return this.assertPlugins(plugins -> plugins.doesNotHavePlugin(plugin));
    }

    ASSERT hasVersion(String version);

    ASSERT hasJavaVersion(String javaVersion);

    ASSERT hasMavenCentralRepository();

    ASSERT assertRepositories(Consumer<GradleRepositoriesAssert> callback);

    ASSERT hasProperties(String... values);

    ASSERT hasDependency(String configuration, String dependency);

    ASSERT hasDependency(GradleModuleDependency dependency);

    ASSERT assertDependencies(Consumer<DependenciesAssert> callback);
}
