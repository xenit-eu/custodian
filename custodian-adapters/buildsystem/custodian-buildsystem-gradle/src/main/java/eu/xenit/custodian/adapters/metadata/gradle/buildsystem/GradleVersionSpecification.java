package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.asserts.build.buildsystem.VersionSpecification;

public interface GradleVersionSpecification extends VersionSpecification {

    static GradleVersionSpecification from(String version) {
        return new DefaultGradleVersionSpecification(version);
    }
}
