package eu.xenit.custodian.adapters.gradle.updates.usecases;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase.GetGradleBuildUpdateProposalsCommand;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase.GradleBuildUpdateProposalsResult;
import eu.xenit.custodian.domain.usecases.UseCase;
import lombok.Value;

public interface GetGradleBuildUpdateProposalsUseCase extends UseCase<GetGradleBuildUpdateProposalsCommand, GradleBuildUpdateProposalsResult> {

    @Override
    GradleBuildUpdateProposalsResult handle(GetGradleBuildUpdateProposalsCommand getGradleBuildUpdateProposalsCommand);

    @Value
    class GetGradleBuildUpdateProposalsCommand {
        GradleBuild build;
    }

    class GradleBuildUpdateProposalsResult {

    }
}
