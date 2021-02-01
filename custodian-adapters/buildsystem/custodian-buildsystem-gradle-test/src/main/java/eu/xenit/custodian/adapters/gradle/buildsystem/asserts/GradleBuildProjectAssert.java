package eu.xenit.custodian.adapters.gradle.buildsystem.asserts;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.RepositoriesAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleProjectAssert;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractPathAssert;

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

    ASSERT hasGroup(String group);

    ASSERT hasVersion(String version);

    ASSERT hasJavaVersion(String javaVersion);

    ASSERT hasMavenCentralRepository();

    ASSERT assertRepositories(Consumer<RepositoriesAssert> callback);

    ASSERT hasProperties(String... values);

    ASSERT hasDependency(String configuration, String dependency);

    default ASSERT hasImplementationDependency(String dependency) {
        return this.hasDependency("implementation", dependency);
    }

    default ASSERT hasTestImplementationDependency(String dependency) {
        return this.hasDependency("testImplementation", dependency);
    }

    ASSERT hasDependency(GradleModuleDependency dependency);

    ASSERT assertDependencies(Consumer<DependenciesAssert> callback);

    ASSERT hasPath(String path);

    ASSERT hasProjectDir(Path projectDir);
    ASSERT assertProjectDir(Consumer<AbstractPathAssert<?>> callback);
}
