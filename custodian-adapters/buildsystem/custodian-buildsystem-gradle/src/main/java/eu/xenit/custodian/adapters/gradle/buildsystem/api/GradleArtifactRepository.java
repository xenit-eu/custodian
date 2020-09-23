package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.ports.spi.buildsystem.Repository;

public interface GradleArtifactRepository extends Repository {

    enum GradleRepositoryType {
        MAVEN,
        IVY
    }

    GradleRepositoryType getType();
}
