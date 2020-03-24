package eu.xenit.custodian.ports.spi.channel;

import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.domain.changes.LogicalChange;
import java.util.Collection;

public interface UpdateChannel {

    Collection<LogicalChange> getChanges(ClonedRepositorySourceMetadata metadata);

}
