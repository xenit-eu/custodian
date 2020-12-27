package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.GradleBuildProjectAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.PluginsAssert;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import lombok.Getter;
import org.assertj.core.api.AbstractStringAssert;

public class GradleBuildFileAssert extends AbstractStringAssert<GradleBuildFileAssert>
        implements GradleBuildProjectAssert<GradleBuildFileAssert> {

    public GradleBuildFileAssert(Path buildDotGradle) throws IOException {
        this(Files.readString(buildDotGradle));
    }
    public GradleBuildFileAssert(String content) {
        super(content, GradleBuildFileAssert.class);
    }

    @Override
    public GradleBuildFileAssert assertPlugins(Consumer<PluginsAssert> callback) {
        var plugins = GradlePluginsStringAssert.from(this.actual);
        callback.accept(plugins);
        return this.myself;
    }

    @Override
    public GradleBuildFileAssert hasVersion(String version) {
        return this.contains("version = '" + version + "'");
    }

    @Override
    public GradleBuildFileAssert hasJavaVersion(String javaVersion) {
        return this.contains("sourceCompatibility = '" + javaVersion + "'");
    }

    @Override
    public GradleBuildFileAssert hasMavenCentralRepository() {
        this.assertRepositories(GradleRepositoriesAssert::hasMavenCentral);
        return myself;
    }


    @Override
    public GradleBuildFileAssert assertRepositories(Consumer<GradleRepositoriesAssert> callback) {
        callback.accept(GradleRepositoriesAssert.from(this.actual));
        return this;
    }

    @Override
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

    @Override
    public GradleBuildFileAssert hasDependency(String configuration, String dependency) {
        this.getDependencies().hasDependency(configuration, dependency);
        return myself;
    }

    @Override
    public GradleBuildFileAssert hasDependency(GradleModuleDependency dependency) {
        DependenciesAssert.from(this.actual).hasDependency(dependency);
        return myself;
    }

    @Getter(lazy=true)
    private final DependenciesAssert dependencies = DependenciesAssert.from(this.actual);

    @Override
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
