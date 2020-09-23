package eu.xenit.custodian.adapters.gradle.updates.usecases;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase.GetGradleBuildUpdateProposalsCommand;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase.GradleBuildUpdateProposalsResult;
import eu.xenit.custodian.domain.usecases.UseCase;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdateProposal;
import java.util.List;
import lombok.Value;

public interface GetGradleBuildUpdateProposalsUseCase extends UseCase<GetGradleBuildUpdateProposalsCommand, GradleBuildUpdateProposalsResult> {

    @Override
    GradleBuildUpdateProposalsResult handle(GetGradleBuildUpdateProposalsCommand getGradleBuildUpdateProposalsCommand);

    @Value
    class GetGradleBuildUpdateProposalsCommand {
        GradleBuild build;
    }

    @Value
    class GradleBuildUpdateProposalsResult {
        List<GradleBuildUpdateProposal> updates;
    }
}
