package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.domain.buildsystem.ModuleSpecification;

public interface MavenModuleSpecification extends ModuleSpecification {

    MavenModuleIdentifier getModuleId();
    MavenVersionSpecification getVersion();

}
