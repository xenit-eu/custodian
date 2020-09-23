package eu.xenit.custodian.adapters.gradle.buildsystem.api.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactSpecification;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleModuleDependency.Builder;
import java.util.Set;

public interface GradleModuleDependencyBuilder {

    GradleModuleDependency build();

    String configuration();
    GradleModuleDependencyBuilder configuration(String configuration);

    String group();
    GradleModuleDependencyBuilder group(String group);

    String name();
    GradleModuleDependencyBuilder name(String name);

    String version();
    GradleModuleDependencyBuilder version(String version);

    Set<GradleArtifactSpecification> artifacts();
    GradleModuleDependencyBuilder artifact(GradleArtifactSpecification artifact);










}
