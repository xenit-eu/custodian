package eu.xenit.custodian.ports.spi.buildsystem;

import java.util.function.Consumer;

public interface BuildModifier {

    BuildModification updateDependency(Project project, Dependency dependency, Consumer<ProjectModuleDependencyModifier> callback);

}
