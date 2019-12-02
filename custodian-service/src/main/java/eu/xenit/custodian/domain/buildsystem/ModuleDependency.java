package eu.xenit.custodian.domain.buildsystem;

import java.util.function.Consumer;

/**
 * A {@code ModuleDependency} is a {@link Dependency} on a module outside the current project.
 */
public interface ModuleDependency extends Dependency {

    // exclude rules
    // transitive properties

    ModuleIdentifier getModuleId();
    VersionSpecification getVersionSpec();


    @Deprecated
    default ModuleIdentifier getId() {
        return this.getModuleId();
    }

//    ArtifactSpecification artifact(Consumer<? super ArtifactSpecification> configureAction);

}
