package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.domain.buildsystem.Dependency;

public interface GradleDependency extends Dependency {

    String getTargetConfiguration();

//    String getGroup();
//    String getName();

}
