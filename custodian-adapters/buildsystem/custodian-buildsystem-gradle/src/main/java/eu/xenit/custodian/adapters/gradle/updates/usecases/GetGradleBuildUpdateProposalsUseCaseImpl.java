package eu.xenit.custodian.adapters.gradle.updates.usecases;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.MavenResolver;
import eu.xenit.custodian.adapters.gradle.updates.impl.dependencies.UpdateMavenDependencyVersion;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdatePort;
import eu.xenit.custodian.adapters.gradle.updates.impl.dependencies.UpdateGradleDependencies;
import eu.xenit.custodian.adapters.gradle.updates.impl.wrapper.UpdateGradleWrapper;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdateProposal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GetGradleBuildUpdateProposalsUseCaseImpl implements GetGradleBuildUpdateProposalsUseCase {

    private final Collection<GradleBuildUpdatePort> gradleUpdatePorts;

    // TODO consider deleting this default constructor ?
    public GetGradleBuildUpdateProposalsUseCaseImpl() {
        this(new UpdateGradleWrapper(),
             new UpdateGradleDependencies(
                     new UpdateMavenDependencyVersion(new MavenResolver())
             ))

        // pinning docker-images ?
        ;
    }

    public GetGradleBuildUpdateProposalsUseCaseImpl(GradleBuildUpdatePort ... ports) {
        this(List.of(ports));
    }

    public GetGradleBuildUpdateProposalsUseCaseImpl(Collection<GradleBuildUpdatePort> gradleUpdatePorts) {

        this.gradleUpdatePorts = gradleUpdatePorts;
    }

    @Override
    public GradleBuildUpdateProposalsResult handle(GetGradleBuildUpdateProposalsCommand command) {

        List<GradleBuildUpdateProposal> updates = this.gradleUpdatePorts.stream()
                .flatMap(updatePort -> updatePort.getUpdateProposals(command.getBuild()))
                .collect(Collectors.toList());

        return new GradleBuildUpdateProposalsResult(updates);
    }
}
