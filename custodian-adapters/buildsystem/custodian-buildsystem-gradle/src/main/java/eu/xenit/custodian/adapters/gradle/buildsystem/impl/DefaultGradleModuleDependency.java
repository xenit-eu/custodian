package eu.xenit.custodian.adapters.gradle.buildsystem.impl;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.DependencyExcludeRule;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactSpecification;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleIdentifier;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleVersionSpecification;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleModuleDependencyBuilder;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

public class DefaultGradleModuleDependency implements GradleModuleDependency {


    public static GradleModuleDependencyBuilder withCoordinates(String group, String name) {
        return new Builder(group, name);
    }

    public static GradleModuleDependency from(String configuration, GradleModuleIdentifier module,
            GradleVersionSpecification version,
            Consumer<GradleModuleDependencyCustomizer> callback) {

        GradleModuleDependencyBuilder builder = DefaultGradleModuleDependency
                .withCoordinates(module.getGroup(), module.getName());

        builder.configuration(configuration);
        builder.version(version.getValue());

        GradleModuleDependencyCustomizer customizer = new GradleModuleDependencyCustomizer(module, version);
        callback.accept(customizer);

//        return new DefaultGradleModuleDependency(configuration, module, version, customizer.getArtifacts());
        return builder.build();
    }

    public static GradleModuleDependency implementation(GradleModuleIdentifier module, String version) {
        return from("implementation", module, GradleVersionSpecification.from(version));
    }

    public static GradleModuleDependency from(String configuration,
            GradleModuleIdentifier module,
            GradleVersionSpecification version) {

        return from(configuration, module, version, dependency -> {
            // should we set default artifact specs here ?
            // or is that not our responsibility ?
        });
    }

    @Getter
    private final GradleModuleIdentifier moduleId;

    @Getter
    private final GradleVersionSpecification versionSpec;

    @Getter
    private final Set<GradleArtifactSpecification> artifacts;

    @Getter
    private final String targetConfiguration;

    DefaultGradleModuleDependency(Builder builder) {
        this.targetConfiguration = builder.configuration;
        this.moduleId = GradleModuleIdentifier.from(builder.group(), builder.name());
        this.versionSpec = GradleVersionSpecification.from(builder.version());
        this.artifacts = builder.artifacts();
    }

    DefaultGradleModuleDependency(String configuration, GradleModuleIdentifier moduleId, GradleVersionSpecification version, Set<GradleArtifactSpecification> artifacts) {
        this.targetConfiguration = configuration;
        this.moduleId = moduleId;
        this.versionSpec = version;
        this.artifacts = artifacts;
    }

    @Override
    public String toString() {
        return String.format("%s(%s:%s)", this.getTargetConfiguration(), this.getId(), this.getVersionSpec().getValue());
    }

    @Override
    public Set<DependencyExcludeRule> getExcludeRules() {
        return Collections.emptySet();
    }

    @Data
    @Accessors(chain = true, fluent = true)
    public static class Builder implements GradleModuleDependencyBuilder {

        private Builder(String group, String name) {
            this.group = group;
            this.name = name;
        }

        private String configuration = "implementation";

        private String group;
        private String name;
        private String version;

        @Getter
        private Set<GradleArtifactSpecification> artifacts = new LinkedHashSet<>();

        @Override
        public Builder artifact(GradleArtifactSpecification artifact) {
            this.artifacts.add(artifact);
            return this;
        }

        /**
         * Build a {@link GradleModuleDependency} with the current state of this builder.
         *
         * @return a {@link GradleModuleDependency}
         */
        @Override
        public GradleModuleDependency build() {
            return new DefaultGradleModuleDependency(this);
        }
    }
}
