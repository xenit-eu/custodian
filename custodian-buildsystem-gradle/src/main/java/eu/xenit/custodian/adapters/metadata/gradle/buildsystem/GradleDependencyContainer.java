package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.domain.metadata.buildsystem.DependencyContainer;

public class GradleDependencyContainer extends DependencyContainer<ExternalModuleIdentifier, GradleModuleDependency> {

    GradleDependencyContainer() {
        super(ExternalModuleIdentifier::from);
    }



}
