package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePluginContainer;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
import eu.xenit.custodian.ports.spi.buildsystem.ProjectContainer;
import java.util.Optional;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class GradleProjectAssert extends AbstractAssert<GradleProjectAssert, GradleProject> {

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

    public GradleProjectAssert hasPlugin(String plugin) {
        return this.assertPlugins(plugins -> {
            plugins.hasPlugin(plugin);
        });
    }

    public GradleProjectAssert withPlugins(Consumer<GradlePluginContainer> callback) {
        this.isNotNull();
        callback.accept(this.actual.getPlugins());
        return this.myself;
    }

    public GradleProjectAssert assertPlugins(Consumer<GradlePluginsAssert> callback) {
        return this.withPlugins(plugins -> {
            callback.accept(new GradlePluginsAssert(plugins));
        });
    }

}
