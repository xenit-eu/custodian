package eu.xenit.custodian.domain.usecases.changes;

import eu.xenit.custodian.domain.usecases.UseCase;
import eu.xenit.custodian.domain.usecases.analysis.GetProjectModelUseCase;
import eu.xenit.custodian.domain.usecases.analysis.GetProjectModelUseCase.GetProjectModelCommand;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.domain.usecases.changes.GetProjectUpdateProposalsUseCase.GetUpdateProposalsCommand;
import java.nio.file.Path;
import lombok.Data;

public interface GetProjectUpdateProposalsUseCase extends UseCase<GetUpdateProposalsCommand, LogicalChangeSet> {

    @Override
    LogicalChangeSet handle(GetUpdateProposalsCommand getUpdateProposalsCommand);

    @Data
    class GetUpdateProposalsCommand {
        private final Path location;
    }
}

class DefaultGetProjectUpdateProposalsUseCase implements GetProjectUpdateProposalsUseCase {

    private final GetProjectModelUseCase projectModelUseCase;

    public DefaultGetProjectUpdateProposalsUseCase(GetProjectModelUseCase projectModelUseCase) {
        this.projectModelUseCase = projectModelUseCase;
    }

    @Override
    public LogicalChangeSet handle(GetUpdateProposalsCommand getUpdateProposalsCommand) {
        GetProjectModelCommand command = new GetProjectModelCommand(getUpdateProposalsCommand.getLocation());
        ProjectModel projectModel = this.projectModelUseCase.handle(command);

        projectModel.buildsystems().builds().forEach(build -> {
//            build.updates().
        });

        throw new UnsupportedOperationException();


    }
}
