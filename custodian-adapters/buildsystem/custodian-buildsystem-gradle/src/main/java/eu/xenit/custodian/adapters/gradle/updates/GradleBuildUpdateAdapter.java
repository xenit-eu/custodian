package eu.xenit.custodian.adapters.gradle.updates;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase.GetGradleBuildUpdateProposalsCommand;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.ports.spi.updates.LogicalChange;
import eu.xenit.custodian.ports.spi.updates.UpdatePort;
import eu.xenit.custodian.util.Arguments;
import java.util.Collection;
import java.util.Collections;

public class GradleBuildUpdateAdapter implements UpdatePort {

    private final GetGradleBuildUpdateProposalsUseCase useCase;

    public GradleBuildUpdateAdapter(GetGradleBuildUpdateProposalsUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public Collection<LogicalChange> getUpdateProposals(ProjectModel projectModel) {
        Arguments.notNull(projectModel, "projectModel");

        var build = projectModel.buildsystems().get(GradleBuildSystem.ID);
        if (build == null) {
            // not a GradleBuild ?
            return Collections.emptyList();
        }

        var gradleBuild = (GradleBuild)build;

        var result = this.useCase.handle(new GetGradleBuildUpdateProposalsCommand(gradleBuild));

        // TODO map result into LogicalChange
        return Collections.emptyList();

    }
}
