package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.asserts.build.buildsystem.Dependency;

public interface GradleDependency extends Dependency {

    String getTargetConfiguration();

//    String getGroup();
//    String getName();

}
