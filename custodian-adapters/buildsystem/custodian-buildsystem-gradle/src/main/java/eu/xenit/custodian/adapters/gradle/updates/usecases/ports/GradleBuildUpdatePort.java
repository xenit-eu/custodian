package eu.xenit.custodian.adapters.gradle.updates.usecases.ports;

// dependency updates
// plugin updates
// gradle wrapper
// custom gradle build code (think: deprecation of a specific dsl) - needs a plugin mechanism !!

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import java.util.stream.Stream;

public interface GradleBuildUpdatePort {

    Stream<BuildUpdateProposal> getUpdateProposals(GradleBuild build);

}
