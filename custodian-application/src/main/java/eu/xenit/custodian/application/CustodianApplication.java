package eu.xenit.custodian.application;

import eu.xenit.custodian.domain.usecases.scm.CloneRepositoryUseCase;
import eu.xenit.custodian.domain.usecases.scm.SourceControlManagementUseCasesFactory;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.domain.usecases.analysis.AnalysisUseCasesFactory;
import eu.xenit.custodian.domain.usecases.analysis.GetProjectModelUseCase;
import eu.xenit.custodian.domain.usecases.buildsystem.BuildSystemsUseCasesFactory;
import eu.xenit.custodian.domain.usecases.buildsystem.GetBuildSystemsUseCase;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.util.Collection;

/**
 * Main class in the application layer.
 *
 * Has a dependency on the secondary adaptors and constructs the
 */
public class CustodianApplication {

    private final BuildSystemsUseCasesFactory buildSystemsUseCasesFactory;
    private final SourceControlManagementUseCasesFactory scmUseCasesFactory;
    private final AnalysisUseCasesFactory analysisUseCasesFactory;


    public CustodianApplication(Collection<BuildSystemPort> buildSystemPorts, Collection<SourceControlHandler> scmHandlers) {
        this.buildSystemsUseCasesFactory = new BuildSystemsUseCasesFactory(buildSystemPorts);
        this.scmUseCasesFactory = new SourceControlManagementUseCasesFactory(scmHandlers);

        this.analysisUseCasesFactory = new AnalysisUseCasesFactory(this::getBuildSystemsUseCase);
    }

    public Custodian getApi() {
        return new CustodianImpl(this.getProjectModelUseCase(), this.getCloneRepositoryUseCase());
    }

    private GetBuildSystemsUseCase getBuildSystemsUseCase() {
        return this.buildSystemsUseCasesFactory.createGetBuildSystemsUseCase();
    }

    private GetProjectModelUseCase getProjectModelUseCase() {
        return this.analysisUseCasesFactory.getProjectModelUseCase();
    }

    private CloneRepositoryUseCase getCloneRepositoryUseCase() {
        return this.scmUseCasesFactory.createCloneRepoUseCase();
    }




}
