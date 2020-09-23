package eu.xenit.custodian.adapters.gradle.updates.usecases.ports;

// dependency updates
// plugin updates
// gradle wrapper
// custom gradle build code (think: deprecation of a specific dsl) - needs a plugin mechanism !!

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import java.util.stream.Stream;

public interface GradleBuildUpdatePort {

    Stream<GradleBuildUpdateProposal> getUpdateProposals(GradleBuild build);

}
