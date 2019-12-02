package eu.xenit.custodian.domain.buildsystem;

public interface ArtifactSpecification {

    ModuleIdentifier getModuleId();
    VersionSpecification getVersionSpec();

}
