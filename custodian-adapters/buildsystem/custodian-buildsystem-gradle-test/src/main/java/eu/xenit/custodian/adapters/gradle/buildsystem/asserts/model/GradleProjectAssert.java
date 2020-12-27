package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePluginContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.GradleBuildProjectAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.PluginsAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.DependenciesAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleRepositoriesAssert;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
import eu.xenit.custodian.ports.spi.buildsystem.ProjectContainer;
import java.util.Optional;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class GradleProjectAssert extends AbstractAssert<GradleProjectAssert, GradleProject>
    implements GradleBuildProjectAssert<GradleProjectAssert> {

    public GradleProjectAssert(GradleProject project) {
        super(project, GradleProjectAssert.class);
    }

    public static GradleProjectAssert assertThat(GradleProject project) {
        return new GradleProjectAssert(project);
    }


    public GradleProjectAssert hasDependency(String notation) {
        this.isNotNull();

        return this.withDependencies(dependencies -> {
            dependencies.hasDependency(notation);
        });
    }

    public GradleProjectAssert withDependencies(Consumer<GradleDependencyContainerAsserts> callback) {
        this.isNotNull();
        callback.accept(new GradleDependencyContainerAsserts(this.actual.getDependencies()));
        return this.myself;
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
    public GradleProjectAssert hasVersion(String version) {
        return null;
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
    public GradleProjectAssert hasDependency(String configuration, String dependency) {
        return null;
    }

    @Override
    public GradleProjectAssert hasDependency(GradleModuleDependency dependency) {
        return null;
    }

    @Override
    public GradleProjectAssert assertDependencies(Consumer<DependenciesAssert> callback) {
        return null;
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
