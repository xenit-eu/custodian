package eu.xenit.custodian.adapters.gradle.buildsystem.api.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleIdentifier;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface GradleDependenciesBuilder {

    GradleDependenciesBuilder add(GradleModuleDependency dependency);

    GradleDependenciesBuilder add(String group, String name, Consumer<GradleModuleDependencyBuilder> callback);

    default GradleDependenciesBuilder add(String group, String name, String version) {
        return this.add(group, name, builder -> builder.version(version));
    }

    default GradleDependenciesBuilder add(String configuration, String group, String name, String version) {
        return this.add(group, name, builder -> builder.configuration(configuration).version(version));
    }

    default GradleDependenciesBuilder implementation(String group, String name, String version) {
        return this.add("implementation", group, name, version);
    }

    default GradleDependenciesBuilder implementation(GradleModuleIdentifier module, String version) {
        return this.implementation(module.getGroup(), module.getName(), version);
    }

    default GradleDependenciesBuilder testImplementation(String group, String name, String version) {
        return this.add("testImplementation", group, name, version);
    }

    Stream<GradleDependency> build();
}
