package eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleDependenciesBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleModuleDependencyBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleModuleDependency.Builder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class DefaultGradleDependenciesBuilder implements GradleDependenciesBuilder {

    // Would this need another data structure ?
    private Collection<GradleModuleDependency> dependencies = new ArrayList<>();

    @Override
    public GradleDependenciesBuilder add(GradleModuleDependency dependency) {
        this.dependencies.add(dependency);
        return this;
    }

    @Override
    public GradleDependenciesBuilder add(String configuration, String group, String artifact, String version) {
        this.dependencies.add(
                DefaultGradleModuleDependency
                        .withCoordinates(group, artifact)
                        .version(version)
                        .configuration(configuration)
                        .build());
        return this;

    }

    @Override
    public Stream<GradleDependency> build() {
        return this.dependencies.stream().map(Function.identity());
    }

    @Override
    public GradleDependenciesBuilder add(String group, String name, Consumer<GradleModuleDependencyBuilder> callback) {
        GradleModuleDependencyBuilder builder = DefaultGradleModuleDependency.withCoordinates(group, name);
        callback.accept(builder);
        this.dependencies.add(builder.build());
        return this;
    }
}
