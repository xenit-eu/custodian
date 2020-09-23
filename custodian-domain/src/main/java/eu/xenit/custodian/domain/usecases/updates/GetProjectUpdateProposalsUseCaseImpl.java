package eu.xenit.custodian.domain.usecases.updates;

import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.ports.spi.updates.UpdatePort;
import java.util.Collection;

class GetProjectUpdateProposalsUseCaseImpl implements GetProjectUpdateProposalsUseCase {

    private final Collection<UpdatePort> updateChannels;

    GetProjectUpdateProposalsUseCaseImpl(Collection<UpdatePort> updateChannels) {
        this.updateChannels = updateChannels;
    }

    @Override
    public GetUpdateProposalsResult handle(GetUpdateProposalsCommand command) {
        ProjectModel projectModel = command.getProjectModel();

        var changes = this.updateChannels.stream().flatMap(channel -> {
            var proposals = channel.getUpdateProposals(projectModel);
            return proposals.stream();
        });

        return new GetUpdateProposalsResult(new DefaultLogicalChangeSet(changes));
    }
}
