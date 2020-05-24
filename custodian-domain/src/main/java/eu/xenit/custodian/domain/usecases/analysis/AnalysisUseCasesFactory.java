package eu.xenit.custodian.domain.usecases.analysis;

import eu.xenit.custodian.domain.usecases.buildsystem.GetBuildSystemsUseCase;
import java.util.function.Supplier;

public class AnalysisUseCasesFactory {

    private final Supplier<GetBuildSystemsUseCase> getBuildSystemsUseCase;

    public AnalysisUseCasesFactory(Supplier<GetBuildSystemsUseCase> getBuildSystemsUseCase) {
        this.getBuildSystemsUseCase = getBuildSystemsUseCase;
    }

    public GetProjectModelUseCase getProjectModelUseCase() {
        return new DefaultGetProjectModelUseCase(this.getBuildSystemsUseCase.get());
    }
}
