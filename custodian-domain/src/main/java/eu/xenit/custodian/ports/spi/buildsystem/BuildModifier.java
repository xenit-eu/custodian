package eu.xenit.custodian.ports.spi.buildsystem;

import eu.xenit.custodian.domain.buildsystem.ModuleDependency;
import java.util.function.Consumer;

public interface BuildModifier {

    BuildModification updateDependency(Project project, ModuleDependency dependency, Consumer<ProjectModuleDependencyModifier> callback);

}
