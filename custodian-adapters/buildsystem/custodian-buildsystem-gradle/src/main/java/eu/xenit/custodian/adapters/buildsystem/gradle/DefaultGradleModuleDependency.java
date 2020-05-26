package eu.xenit.custodian.adapters.buildsystem.gradle;

import java.util.Set;
import lombok.Getter;

public class DefaultGradleModuleDependency implements GradleModuleDependency {

    @Getter
    private final GradleModuleIdentifier moduleId;

    @Getter
    private final GradleVersionSpecification versionSpec;

    @Getter
    private final Set<GradleArtifactSpecification> artifacts;

    @Getter
    private final String targetConfiguration;

    DefaultGradleModuleDependency(String configuration, GradleModuleIdentifier moduleId, GradleVersionSpecification version, Set<GradleArtifactSpecification> artifacts) {
        this.targetConfiguration = configuration;
        this.moduleId = moduleId;
        this.versionSpec = version;
        this.artifacts = artifacts;
    }

}
