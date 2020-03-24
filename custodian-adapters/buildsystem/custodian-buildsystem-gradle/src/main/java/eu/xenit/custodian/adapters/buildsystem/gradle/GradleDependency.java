package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.domain.buildsystem.Dependency;

public interface GradleDependency extends Dependency {

    String getTargetConfiguration();

//    String getGroup();
//    String getName();

}
