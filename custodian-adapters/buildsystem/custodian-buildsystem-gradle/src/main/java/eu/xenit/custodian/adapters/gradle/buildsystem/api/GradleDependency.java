package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.ports.spi.buildsystem.Dependency;

public interface GradleDependency extends Dependency {

    /**
     * Returns the group of this dependency. Might return null.
     */
    String getGroup();

    /**
     * Returns the name of this dependency. Never returns null.
     */
    String getName();

    /**
     * Returns the version of this dependency. Might return null.
     */
    String getVersion();

    String getTargetConfiguration();

}
