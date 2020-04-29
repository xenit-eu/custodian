package eu.xenit.custodian.domain.buildsystem;

public interface ModuleVersionIdentifier {

    String getId();

    ModuleIdentifier getModuleId();
    ModuleVersion getVersion();


}
