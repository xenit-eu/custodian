package eu.xenit.custodian.adapters.gradle.updates.impl.wrapper;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdatePort;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdateProposal;
import java.util.stream.Stream;

public class UpdateGradleWrapper implements GradleBuildUpdatePort {

    @Override
    public Stream<GradleBuildUpdateProposal> getUpdateProposals(GradleBuild build) {
        return Stream.empty();
    }
}
