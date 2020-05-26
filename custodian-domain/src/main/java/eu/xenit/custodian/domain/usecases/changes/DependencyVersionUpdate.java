package eu.xenit.custodian.domain.usecases.changes;

import eu.xenit.custodian.domain.buildsystem.VersionSpecification;
import eu.xenit.custodian.ports.spi.buildsystem.Dependency;
import java.util.Optional;

public interface DependencyVersionUpdate extends LogicalChange {

    Dependency getDependency();
    VersionSpecification getCurrentVersion();
    Optional<? extends VersionSpecification> getProposedVersion();

}
