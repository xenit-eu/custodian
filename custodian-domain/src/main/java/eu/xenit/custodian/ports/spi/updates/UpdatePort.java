package eu.xenit.custodian.ports.spi.updates;

import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import java.util.Collection;

public interface UpdatePort {

    Collection<LogicalChange> getUpdateProposals(ProjectModel projectModel);

}
