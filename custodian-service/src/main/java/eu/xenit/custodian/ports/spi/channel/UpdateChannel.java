package eu.xenit.custodian.ports.spi.channel;

import eu.xenit.custodian.asserts.build.changes.LogicalChange;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import java.util.Collection;

public interface UpdateChannel {

    Collection<LogicalChange> getChanges(ClonedRepositorySourceMetadata metadata);

}
