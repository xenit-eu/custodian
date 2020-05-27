package eu.xenit.custodian.adapters.gradle.updates.usecases;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.MavenResolver;
import eu.xenit.custodian.adapters.gradle.updates.impl.dependencies.UpdateMavenDependencyVersion;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdatePort;
import eu.xenit.custodian.adapters.gradle.updates.impl.dependencies.UpdateDependencies;
import eu.xenit.custodian.adapters.gradle.updates.impl.wrapper.UpdateGradleWrapper;
import java.util.Collection;
import java.util.List;

public class GetGradleBuildUpdateProposalsUseCaseImpl implements GetGradleBuildUpdateProposalsUseCase {

    private final Collection<GradleBuildUpdatePort> gradleBuildUpdates;

    public GetGradleBuildUpdateProposalsUseCaseImpl() {
        this(new UpdateGradleWrapper(),
             new UpdateDependencies(
                     new UpdateMavenDependencyVersion(new MavenResolver())
             ))

        // pinning docker-images ?
        ;
    }

    public GetGradleBuildUpdateProposalsUseCaseImpl(GradleBuildUpdatePort ... ports) {
        this(List.of(ports));
    }

    public GetGradleBuildUpdateProposalsUseCaseImpl(Collection<GradleBuildUpdatePort> gradleBuildUpdates) {

        this.gradleBuildUpdates = gradleBuildUpdates;
    }

    @Override
    public GradleBuildUpdateProposalsResult handle(GetGradleBuildUpdateProposalsCommand command) {
        return null;
    }
}
