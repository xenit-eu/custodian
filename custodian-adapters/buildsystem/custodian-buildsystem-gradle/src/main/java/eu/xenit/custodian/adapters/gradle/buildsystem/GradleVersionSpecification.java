package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.domain.buildsystem.VersionSpecification;

public interface GradleVersionSpecification extends VersionSpecification {

    static GradleVersionSpecification from(String version) {
        return new DefaultGradleVersionSpecification(version);
    }
}
