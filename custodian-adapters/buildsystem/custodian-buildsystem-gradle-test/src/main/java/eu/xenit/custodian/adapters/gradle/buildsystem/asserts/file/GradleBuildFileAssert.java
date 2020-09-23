package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import lombok.Getter;
import org.assertj.core.api.AbstractStringAssert;

public class GradleBuildFileAssert extends AbstractStringAssert<GradleBuildFileAssert> {

    public GradleBuildFileAssert(Path buildDotGradle) throws IOException {
        this(Files.readString(buildDotGradle));
    }
    public GradleBuildFileAssert(String content) {
        super(content, GradleBuildFileAssert.class);
    }

    public GradleBuildFileAssert hasPlugin(String plugin) {
        this.getPluginAssert().hasPlugin(plugin);
        return myself;
    }

    public GradleBuildFileAssert hasPlugin(String plugin, String version) {
        this.getPluginAssert().hasPlugin(plugin, version);
        return myself;
    }

    public GradleBuildFileAssert hasJavaPlugin() {
        this.hasPlugin("java");
        return myself;
    }

    public GradleBuildFileAssert doesNotHavePlugin(String plugin) {
        this.getPluginAssert().doesNotHavePlugin(plugin);
        return myself;
    }

    @Getter(lazy=true)
    private final PluginAssert pluginAssert = PluginAssert.from(this.actual);

    public GradleBuildFileAssert hasVersion(String version) {
        return this.contains("version = '" + version + "'");
    }

    public GradleBuildFileAssert hasJavaVersion(String javaVersion) {
        return this.contains("sourceCompatibility = '" + javaVersion + "'");
    }

    public GradleBuildFileAssert hasMavenCentralRepository() {
        this.assertRepositories(GradleRepositoriesAssert::hasMavenCentral);
        return myself;
    }


    public GradleBuildFileAssert assertRepositories(Consumer<GradleRepositoriesAssert> callback) {
        callback.accept(GradleRepositoriesAssert.from(this.actual));
        return this;
    }

    public GradleBuildFileAssert hasProperties(String... values) {
        StringBuilder builder = new StringBuilder(String.format("ext {%n"));
        if (values.length % 2 == 1) {
            throw new IllegalArgumentException("Size must be even, it is a set of property=value pairs");
        } else {
            for(int i = 0; i < values.length; i += 2) {
                builder.append(String.format("\tset('%s', '%s')%n", values[i], values[i + 1]));
            }

            builder.append("}");
            return this.contains(builder.toString());
        }
    }

    public GradleBuildFileAssert hasDependency(String configuration, String dependency) {
        this.getDependencies().hasDependency(configuration, dependency);
        return myself;
    }

    public GradleBuildFileAssert hasDependency(GradleModuleDependency dependency) {
        DependenciesAssert.from(this.actual).hasDependency(dependency);
        return myself;
    }

    @Getter(lazy=true)
    private final DependenciesAssert dependencies = DependenciesAssert.from(this.actual);

    public GradleBuildFileAssert assertDependencies(Consumer<DependenciesAssert> callback) {
        callback.accept(this.getDependencies());
        return myself;
    }

    public GradleTaskAssert getTask(String task) {
        return GradleTaskAssert.from(task, this.actual);
    }

    public GradleBuildFileAssert assertTask(String task, Consumer<GradleTaskAssert> callback) {
        callback.accept(this.getTask(task));
        return myself;
    }


}
