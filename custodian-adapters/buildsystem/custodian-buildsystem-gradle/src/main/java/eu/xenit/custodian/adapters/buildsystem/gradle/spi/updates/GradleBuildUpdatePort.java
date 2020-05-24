package eu.xenit.custodian.adapters.buildsystem.gradle.spi.updates;

// dependency updates
// plugin updates
// gradle wrapper
// custom gradle build code (think: deprecation of a specific dsl) - needs a plugin mechanism !!

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import java.util.stream.Stream;

public interface GradleBuildUpdatePort {

    Stream<BuildUpdateProposal> getUpdateProposals(GradleBuild build);

}
