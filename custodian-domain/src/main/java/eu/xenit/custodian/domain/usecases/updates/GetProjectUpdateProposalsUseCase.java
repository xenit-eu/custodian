package eu.xenit.custodian.domain.usecases.updates;

import eu.xenit.custodian.domain.usecases.UseCase;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.domain.usecases.updates.GetProjectUpdateProposalsUseCase.GetUpdateProposalsCommand;
import lombok.Data;

public interface GetProjectUpdateProposalsUseCase extends UseCase<GetUpdateProposalsCommand, LogicalChangeSet> {

    @Override
    LogicalChangeSet handle(GetUpdateProposalsCommand getUpdateProposalsCommand);

    @Data
    class GetUpdateProposalsCommand {
        private final ProjectModel projectModel;
    }
}

