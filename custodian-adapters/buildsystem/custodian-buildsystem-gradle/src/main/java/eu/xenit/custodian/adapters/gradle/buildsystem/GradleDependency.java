package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.ports.spi.buildsystem.Dependency;

public interface GradleDependency extends Dependency {

    String getTargetConfiguration();

//    String getGroup();
//    String getName();

}
