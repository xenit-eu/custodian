package eu.xenit.custodian.domain.buildsystem;

public interface ArtifactIdentifier {

    ModuleVersionIdentifier getModuleVersionId();
    ModuleVersion getVersion();


}
