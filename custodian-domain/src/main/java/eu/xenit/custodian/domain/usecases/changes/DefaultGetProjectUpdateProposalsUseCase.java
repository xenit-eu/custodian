package eu.xenit.custodian.domain.usecases.changes;

import eu.xenit.custodian.domain.usecases.analysis.GetProjectModelUseCase;
import eu.xenit.custodian.domain.usecases.analysis.GetProjectModelUseCase.GetProjectModelCommand;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;

class DefaultGetProjectUpdateProposalsUseCase implements GetProjectUpdateProposalsUseCase {

    private final GetProjectModelUseCase projectModelUseCase;

    DefaultGetProjectUpdateProposalsUseCase(GetProjectModelUseCase projectModelUseCase) {
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
