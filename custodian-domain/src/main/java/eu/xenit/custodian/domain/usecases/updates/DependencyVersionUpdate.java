package eu.xenit.custodian.domain.usecases.updates;

import eu.xenit.custodian.domain.buildsystem.VersionSpecification;
import eu.xenit.custodian.ports.spi.buildsystem.Dependency;
import eu.xenit.custodian.ports.spi.updates.LogicalChange;
import java.util.Optional;

public interface DependencyVersionUpdate extends LogicalChange {

    Dependency getDependency();
    VersionSpecification getCurrentVersion();
    Optional<? extends VersionSpecification> getProposedVersion();

}
