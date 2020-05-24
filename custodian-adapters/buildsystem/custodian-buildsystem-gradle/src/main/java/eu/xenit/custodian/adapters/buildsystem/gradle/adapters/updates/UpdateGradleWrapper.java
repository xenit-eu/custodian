package eu.xenit.custodian.adapters.buildsystem.gradle.adapters.updates;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.adapters.buildsystem.gradle.spi.updates.GradleBuildUpdatePort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import java.util.stream.Stream;

public class UpdateGradleWrapper implements GradleBuildUpdatePort {

    @Override
    public Stream<BuildUpdateProposal> getUpdateProposals(GradleBuild build) {
        return Stream.empty();
    }
}
