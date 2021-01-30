package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDependencyContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePluginContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.GradleBuildProjectAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.PluginsAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.DependenciesAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleBuildFileAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleRepositoriesAssert;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
import eu.xenit.custodian.ports.spi.buildsystem.ProjectContainer;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractPathAssert;
import org.assertj.core.api.Assertions;

public class GradleProjectAssert extends AbstractAssert<GradleProjectAssert, GradleProject>
    implements GradleBuildProjectAssert<GradleProjectAssert> {

    public GradleProjectAssert(GradleProject project) {
        super(project, GradleProjectAssert.class);
    }

    public static GradleProjectAssert assertThat(GradleProject project) {
        return new GradleProjectAssert(project);
    }



    public GradleProjectAssert withChildProjects(Consumer<ProjectContainer> consumer) {
        consumer.accept(this.actual.getChildProjects());
        return myself;
    }

    public GradleProjectAssert hasParent(Consumer<Optional<? extends Project>> callback) {
        callback.accept(this.actual.getParent());
        return myself;
    }

    public GradleProjectAssert hasName(String custodian) {
        Assertions.assertThat(this.actual.getName()).isEqualTo(custodian);
        return myself;
    }

    @Override
    public GradleProjectAssert hasGroup(String group) {
        Assertions.assertThat(this.actual.getGroup()).isEqualTo(group);
        return myself;
    }

    @Override
    public GradleProjectAssert hasVersion(String version) {
        Assertions.assertThat(this.actual.getVersion()).isEqualTo(version);
        return myself;
    }

    @Override
    public GradleProjectAssert hasJavaVersion(String javaVersion) {
        return null;
    }

    @Override
    public GradleProjectAssert hasMavenCentralRepository() {
        return null;
    }

    @Override
    public GradleProjectAssert assertRepositories(Consumer<GradleRepositoriesAssert> callback) {
        return null;
    }

    @Override
    public GradleProjectAssert hasProperties(String... values) {
        return null;
    }



    @Override
    public GradleProjectAssert hasDependency(GradleModuleDependency dependency) {
        return null;
    }

    @Override
    public GradleProjectAssert hasDependency(String configuration, String dependency) {
        return this.assertDependencies(dependencies -> {
            dependencies.hasDependency(configuration, dependency);
        });
    }

    @Override
    public GradleProjectAssert assertDependencies(Consumer<DependenciesAssert> callback) {
        return this.withDependencies(dependencies -> {
            callback.accept(new GradleDependencyContainerAsserts(dependencies));
        });
    }

    @Override
    public GradleProjectAssert hasPath(String path) {
        Assertions.assertThat(this.actual.getPath()).isEqualTo(path);
        return this.myself;
    }

    public GradleProjectAssert assertProjectDir(Consumer<AbstractPathAssert<?>> callback) {
        callback.accept(Assertions.assertThat(this.actual.getProjectDir()));
        return this.myself;
    }

    /**
     * Assert that the tested project has the expected project directory {@link Path}.
     *
     * The expected project directory provided will be converted to an absolute path
     * with  {@link Path#toAbsolutePath()} and normalized with {@link Path#normalize()}
     * before performing the actual test.
     *
     * @param projectDir the expected path of the project
     * @return self
     */
    @Override
    public GradleProjectAssert hasProjectDir(Path projectDir) {
        Assertions.assertThat(this.actual.getProjectDir())
                .isEqualByComparingTo(projectDir.toAbsolutePath().normalize());
        return this.myself;
    }

    public GradleProjectAssert withDependencies(Consumer<GradleDependencyContainer> callback) {
        this.isNotNull();
        callback.accept(this.actual.getDependencies());
        return this.myself;
    }


    public GradleProjectAssert withPlugins(Consumer<GradlePluginContainer> callback) {
        this.isNotNull();
        callback.accept(this.actual.getPlugins());
        return this.myself;
    }

    public GradleProjectAssert assertPlugins(Consumer<PluginsAssert> callback) {
        return this.withPlugins(plugins -> {
            callback.accept(new GradleModelPluginsAssert(plugins));
        });
    }

}
