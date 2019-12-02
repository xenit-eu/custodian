package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.domain.buildsystem.GroupArtifactModuleIdentifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class DefaultGradleArtifactSpecification implements GradleArtifactSpecification {

    private final GroupArtifactModuleIdentifier moduleId;
    private final GradleVersionSpecification versionSpec;
    private final String name;
    private final String type;
    private final String extension;
    private final String classifier;

}
