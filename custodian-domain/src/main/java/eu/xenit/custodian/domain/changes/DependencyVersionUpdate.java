package eu.xenit.custodian.domain.changes;

import eu.xenit.custodian.domain.buildsystem.ModuleDependency;
import eu.xenit.custodian.domain.buildsystem.VersionSpecification;
import java.util.Optional;

public interface DependencyVersionUpdate extends LogicalChange {

    ModuleDependency getDependency();
    Optional<? extends VersionSpecification> getProposedVersion();

}
