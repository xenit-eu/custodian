package eu.xenit.custodian.ports.spi.channel;

import eu.xenit.custodian.domain.changes.ChangeSet;
import eu.xenit.custodian.domain.changes.ChangeSetCollection;
import eu.xenit.custodian.domain.metadata.ProjectMetadata;
import java.util.Collection;

public interface UpdateChannel {

    Collection<ChangeSet> getChangeSets(ProjectMetadata metadata);

}
