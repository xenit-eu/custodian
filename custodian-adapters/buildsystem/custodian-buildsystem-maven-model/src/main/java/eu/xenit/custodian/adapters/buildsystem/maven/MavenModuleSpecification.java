package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.asserts.build.buildsystem.ModuleSpecification;

public interface MavenModuleSpecification extends ModuleSpecification {

    MavenModuleIdentifier getModuleId();
    MavenVersionSpecification getVersion();

}
