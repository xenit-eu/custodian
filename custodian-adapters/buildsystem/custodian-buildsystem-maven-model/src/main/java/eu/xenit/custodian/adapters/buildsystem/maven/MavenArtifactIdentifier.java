package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.domain.buildsystem.ArtifactIdentifier;

public interface MavenArtifactIdentifier extends ArtifactIdentifier {


    MavenModuleVersionIdentifier getModuleVersionId();

    // ModuleVersion getVersion();

    // type is not always the same as extension
    // see https://maven.apache.org/ref/3.6.2/maven-core/artifact-handlers.html
    String getType();
    String getExtension();

    String getClassifier();



}
