package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.adapters.buildsystem.gradle.spi.updates.GradleBuildUpdatePort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdates;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public class GradleBuildUpdates implements BuildUpdates {

    private final GradleBuild gradleBuild;
    private final Collection<GradleBuildUpdatePort> updatePorts;

    public GradleBuildUpdates(GradleBuild gradleBuild,
            Collection<GradleBuildUpdatePort> updatePorts) {
        this.gradleBuild = gradleBuild;
        this.updatePorts = updatePorts;
    }

    @Override
    public Stream<BuildUpdateProposal> proposals() {
        return this.updatePorts.stream()
                .map(updates -> updates.getUpdateProposals(this.gradleBuild))
                .flatMap(Function.identity());
    }


    // list all possible gradle updates ?

}
