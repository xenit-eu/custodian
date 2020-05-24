package eu.xenit.custodian.domain.scm;

import eu.xenit.custodian.ports.api.SourceRepositoryReference;

public interface CloneSourceRepositoryUseCase {

    void cloneRepository(CloneRepositoryCommand command);

    class CloneRepositoryCommand {
        SourceRepositoryReference repositoryReference;
    }
}
