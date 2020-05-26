package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.ports.spi.buildsystem.Dependency;

public interface GradleDependency extends Dependency {

    String getTargetConfiguration();

//    String getGroup();
//    String getName();

}
