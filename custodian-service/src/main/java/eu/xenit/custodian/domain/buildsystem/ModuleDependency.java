package eu.xenit.custodian.domain.buildsystem;

import java.util.function.Consumer;

/**
 * A {@code ModuleDependency} is a {@link Dependency} on a module outside the current project.
 *
 * This implies it could be a child/parent/sibling/linked project.
 */
public interface ModuleDependency extends Dependency {

    // exclude rules
    // transitive properties

    ModuleIdentifier getModuleId();
    VersionSpecification getVersionSpec();

    default String getId() {
        return this.getModuleId().getId();
    }
}
