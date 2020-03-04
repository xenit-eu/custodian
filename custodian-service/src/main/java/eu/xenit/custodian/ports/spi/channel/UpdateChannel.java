package eu.xenit.custodian.ports.spi.channel;

import eu.xenit.custodian.domain.changes.LogicalChange;
import eu.xenit.custodian.ports.api.ProjectMetadata;
import java.util.Collection;

public interface UpdateChannel {

    Collection<LogicalChange> getChanges(ProjectMetadata metadata);

}
