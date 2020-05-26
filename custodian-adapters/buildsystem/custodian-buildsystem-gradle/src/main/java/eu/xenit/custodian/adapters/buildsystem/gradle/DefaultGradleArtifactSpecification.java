package eu.xenit.custodian.adapters.buildsystem.gradle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class DefaultGradleArtifactSpecification implements GradleArtifactSpecification {

    private final GradleModuleIdentifier moduleId;
    private final GradleVersionSpecification versionSpec;
    private final String name;
    private final String type;
    private final String extension;
    private final String classifier;

}
