package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.DependenciesAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.GradleBuildProjectAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.PluginsAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.RepositoriesAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleProjectAssert;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import lombok.Getter;
import org.assertj.core.api.AbstractPathAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;

public class GradleBuildFileAssert extends AbstractStringAssert<GradleBuildFileAssert>
        implements GradleBuildProjectAssert<GradleBuildFileAssert> {

    private final Path buildDotGradlePath;

    public GradleBuildFileAssert(Path buildDotGradle) throws IOException {
        super(Files.readString(buildDotGradle), GradleBuildFileAssert.class);
        this.buildDotGradlePath = buildDotGradle;
    }

    public GradleBuildFileAssert(String content) {
        super(content, GradleBuildFileAssert.class);
        this.buildDotGradlePath = null;
    }

    @Override
    public GradleBuildFileAssert assertPlugins(Consumer<PluginsAssert> callback) {
        var plugins = GradlePluginsStringAssert.from(this.actual);
        callback.accept(plugins);
        return this.myself;
    }

    @Override
    public GradleBuildFileAssert hasGroup(String group) {
        return this.contains("group = '" + group + "'");
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
        this.assertRepositories(RepositoriesAssert::hasMavenCentral);
        return myself;
    }


    @Override
    public GradleBuildFileAssert assertRepositories(Consumer<RepositoriesAssert> callback) {
        callback.accept(GradleRepositoriesAssert.from(this.actual));
        return this;
    }

    @Override
    public GradleBuildFileAssert hasProperties(String... values) {
        StringBuilder builder = new StringBuilder(String.format("ext {%n"));
        if (values.length % 2 == 1) {
            throw new IllegalArgumentException("Size must be even, it is a set of property=value pairs");
        } else {
            for (int i = 0; i < values.length; i += 2) {
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
        DependenciesFileAssert.from(this.actual).hasDependency(dependency);
        return myself;
    }

    @Getter(lazy = true)
    private final DependenciesFileAssert dependencies = DependenciesFileAssert.from(this.actual);

    @Override
    public GradleBuildFileAssert assertDependencies(Consumer<DependenciesAssert> callback) {
        callback.accept(this.getDependencies());
        return myself;
    }

    @Override
    public GradleBuildFileAssert hasPath(String path) {
        // can't verify the project-hierarchy from a single build.gradle ?
        return this.myself;
    }

    @Override
    public GradleBuildFileAssert hasProjectDir(Path projectDir) {
        // this assertion might just have the String content of the build.gradle and not the location
        // in that case, should the assertion fail ?
        if (this.buildDotGradlePath == null) {
            Assertions.fail("{} instantiated with the contents of build.gradle, not the {}",
                    GradleBuildFileAssert.class, Path.class);
        }

        Assertions.assertThat(this.buildDotGradlePath).isEqualByComparingTo(projectDir);
        return this.myself;
    }

    @Override
    public GradleBuildFileAssert assertProjectDir(Consumer<AbstractPathAssert<?>> callback) {
        Assertions.fail("assertion not implemented");
        return this.myself;
    }

    public GradleTaskAssert getTask(String task) {
        return GradleTaskAssert.from(task, this.actual);
    }

    public GradleBuildFileAssert assertTask(String task, Consumer<GradleTaskAssert> callback) {
        callback.accept(this.getTask(task));
        return myself;
    }


}
