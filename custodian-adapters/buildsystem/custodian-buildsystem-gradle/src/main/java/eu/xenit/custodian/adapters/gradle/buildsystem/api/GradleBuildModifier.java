package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.ports.spi.buildsystem.BuildModification;
import eu.xenit.custodian.ports.spi.buildsystem.ProjectModuleDependencyModifier;
import java.util.function.Consumer;


public interface GradleBuildModifier {

    BuildModification updateDependency(GradleProject project, GradleModuleDependency dependency, Consumer<ProjectModuleDependencyModifier> callback);

}
