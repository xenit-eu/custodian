package eu.xenit.custodian.adapters.gradle.updates.impl.wrapper;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdatePort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import java.util.stream.Stream;

public class UpdateGradleWrapper implements GradleBuildUpdatePort {

    @Override
    public Stream<BuildUpdateProposal> getUpdateProposals(GradleBuild build) {
        return Stream.empty();
    }
}
