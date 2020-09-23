package eu.xenit.custodian.domain.usecases.updates;

import eu.xenit.custodian.domain.usecases.UseCase;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.domain.usecases.updates.GetProjectUpdateProposalsUseCase.GetUpdateProposalsCommand;
import eu.xenit.custodian.domain.usecases.updates.GetProjectUpdateProposalsUseCase.GetUpdateProposalsResult;
import lombok.Data;
import lombok.Value;

public interface GetProjectUpdateProposalsUseCase extends UseCase<GetUpdateProposalsCommand, GetUpdateProposalsResult> {

    @Override
    GetUpdateProposalsResult handle(GetUpdateProposalsCommand getUpdateProposalsCommand);

    @Value
    class GetUpdateProposalsCommand {
        final ProjectModel projectModel;
    }

    @Value
    class GetUpdateProposalsResult {
        LogicalChangeSet changeSet;
    }
}

