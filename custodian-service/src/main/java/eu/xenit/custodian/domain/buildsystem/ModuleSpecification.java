package eu.xenit.custodian.domain.buildsystem;

public interface ModuleSpecification {

    ModuleIdentifier getModuleId();
    VersionSpecification getVersion();

}
