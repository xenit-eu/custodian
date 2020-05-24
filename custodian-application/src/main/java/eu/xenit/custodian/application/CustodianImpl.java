package eu.xenit.custodian.application;

import eu.xenit.custodian.domain.usecases.scm.CloneRepositoryUseCase;
import eu.xenit.custodian.domain.usecases.scm.CloneRepositoryUseCase.CloneRepositoryUseCaseCommand;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.domain.usecases.changes.DefaultLogicalChangeSet;
import eu.xenit.custodian.domain.usecases.changes.LogicalChangeSet;
import eu.xenit.custodian.domain.project.ProjectReferenceParser;
import eu.xenit.custodian.domain.usecases.analysis.GetProjectModelUseCase;
import eu.xenit.custodian.domain.usecases.analysis.GetProjectModelUseCase.GetProjectModelCommand;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.io.IOException;
import java.util.Collections;

public class CustodianImpl implements Custodian {

    private final GetProjectModelUseCase getProjectModelUseCase;
    private final CloneRepositoryUseCase cloneRepositoryUseCase;

    CustodianImpl(GetProjectModelUseCase getProjectModelUseCase, CloneRepositoryUseCase cloneRepositoryUseCase) {

        this.getProjectModelUseCase = getProjectModelUseCase;
        this.cloneRepositoryUseCase = cloneRepositoryUseCase;
    }

    @Override
    public SourceRepositoryReference createReference(String repositoryReference) {
        return ProjectReferenceParser.from(repositoryReference);
    }

    @Override
    public ClonedRepositoryHandle checkoutProject(SourceRepositoryReference reference) throws IOException {
        var result = this.cloneRepositoryUseCase.handle(new CloneRepositoryUseCaseCommand(reference));
        return result.getHandle();
    }

    @Override
    public ProjectModel analyzeProjectModel(ClonedRepositoryHandle handle) {

        GetProjectModelCommand command = new GetProjectModelCommand(handle.getLocation());
        return this.getProjectModelUseCase.handle(command);
    }

    @Override
    public LogicalChangeSet getChanges(ClonedRepositorySourceMetadata metadata) {
//        return new DefaultLogicalChangeSet(this.channel.getChanges(metadata));
        return new DefaultLogicalChangeSet(Collections.emptyList());
    }
}
