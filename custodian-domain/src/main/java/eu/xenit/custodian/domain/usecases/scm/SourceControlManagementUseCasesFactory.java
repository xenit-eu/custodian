package eu.xenit.custodian.domain.usecases.scm;

import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import eu.xenit.custodian.util.Arguments;
import java.util.Collection;
import java.util.List;

public class SourceControlManagementUseCasesFactory {

    private final List<SourceControlHandler> scmHandlers;

    public SourceControlManagementUseCasesFactory(Collection<SourceControlHandler> scmHandlers) {
        Arguments.notNull(scmHandlers, "scmHandlers");
        this.scmHandlers = List.copyOf(scmHandlers);
    }

    public CloneRepositoryUseCase createCloneRepoUseCase() {
        return new DefaultCloneRepositoryUseCase(this.scmHandlers);
    }
}
