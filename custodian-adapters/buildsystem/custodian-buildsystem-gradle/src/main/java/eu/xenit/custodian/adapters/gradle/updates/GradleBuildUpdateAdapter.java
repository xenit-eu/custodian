package eu.xenit.custodian.adapters.gradle.updates;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCase.GetGradleBuildUpdateProposalsCommand;
import eu.xenit.custodian.adapters.gradle.updates.usecases.GetGradleBuildUpdateProposalsUseCaseImpl;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdatePort;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.domain.usecases.updates.ChangeApplicationResult;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdateProposal;
import eu.xenit.custodian.domain.usecases.updates.Patch;
import eu.xenit.custodian.ports.spi.buildsystem.BuildModification;
import eu.xenit.custodian.ports.spi.updates.LogicalChange;
import eu.xenit.custodian.ports.spi.updates.UpdatePort;
import eu.xenit.custodian.util.Arguments;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class GradleBuildUpdateAdapter implements UpdatePort {

    private final GetGradleBuildUpdateProposalsUseCase useCase;

    public GradleBuildUpdateAdapter(Collection<GradleBuildUpdatePort> gradleUpdatePorts) {
        this(new GetGradleBuildUpdateProposalsUseCaseImpl(gradleUpdatePorts));
    }

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

        return result.getUpdates().stream()
                .map(BuildUpdateProposal2LogicalChangeAdapter::new)
                .collect(Collectors.toList());

    }

    @RequiredArgsConstructor
    static class BuildUpdateProposal2LogicalChangeAdapter implements LogicalChange {

        @Getter
        private final GradleBuildUpdateProposal proposal;

        @Override
        public String getDescription() {
            return this.proposal.getDescription();
        }

        @Override
        public ChangeApplicationResult apply() {
            var modification = this.proposal.apply();
            return new BuildModification2ChangeApplicationResult(modification, this);
        }
    }

    @RequiredArgsConstructor
    static class BuildModification2ChangeApplicationResult implements ChangeApplicationResult {

        @Getter
        private final BuildModification buildModification;

        @Getter
        private final LogicalChange logicalChange;

        @Override
        public boolean isSuccess() {
            return !this.buildModification.isEmpty();
        }

        @Override
        public String getShortDescription() {
            return this.logicalChange.getDescription();
        }

        @Override
        public Stream<Patch> getPatches() {
            return this.buildModification.getPatches();
        }
    }
}
