package eu.xenit.custodian.domain.usecases.updates;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.domain.usecases.updates.GetProjectUpdateProposalsUseCase.GetUpdateProposalsCommand;
import eu.xenit.custodian.ports.spi.updates.UpdatePort;
import java.util.List;
import org.junit.jupiter.api.Test;

class GetProjectUpdateProposalsUseCaseImplTest {

    @Test
    void testEveryUpdatePort_getUpdateProposals_isCalled() {
        var updatePort1 = mock(UpdatePort.class);
        var updatePort2 = mock(UpdatePort.class);
        var projectModel = mock(ProjectModel.class);

        var updateProposalsUseCase = new GetProjectUpdateProposalsUseCaseImpl(List.of(updatePort1, updatePort2));
        var command = new GetUpdateProposalsCommand(projectModel);

        var proposals = updateProposalsUseCase.handle(command);

        verify(updatePort1).getUpdateProposals(projectModel);
        verify(updatePort2).getUpdateProposals(projectModel);
    }
}