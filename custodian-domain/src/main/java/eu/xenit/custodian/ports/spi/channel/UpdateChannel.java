package eu.xenit.custodian.ports.spi.channel;

import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.domain.usecases.changes.LogicalChange;
import java.util.Collection;

public interface UpdateChannel {

    Collection<LogicalChange> getChanges(ProjectModel projectModel);

}
