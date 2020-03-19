package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationDescriptor;
import eu.xenit.custodian.asserts.build.buildsystem.GroupArtifactModuleIdentifier;
import java.util.Set;
import lombok.Getter;

public class DefaultGradleModuleDependency implements GradleModuleDependency {

    @Getter
    private final GroupArtifactModuleIdentifier moduleId;

    @Getter
    private final GradleVersionSpecification versionSpec;

    @Getter
    private final Set<GradleArtifactSpecification> artifacts;

    @Getter
    private final String targetConfiguration;

    DefaultGradleModuleDependency(String configuration, GroupArtifactModuleIdentifier moduleId, GradleVersionSpecification version, Set<GradleArtifactSpecification> artifacts) {
        this.targetConfiguration = configuration;
        this.moduleId = moduleId;
        this.versionSpec = version;
        this.artifacts = artifacts;
    }

    @Override
    public MavenArtifactSpecificationDescriptor getMavenArtifactsDescriptor() {
        return new MavenArtifactSpecificationDescriptorAdaptor(this);
    }

}
