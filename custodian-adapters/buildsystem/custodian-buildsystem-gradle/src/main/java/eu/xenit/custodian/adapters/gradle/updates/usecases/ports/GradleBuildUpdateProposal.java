package eu.xenit.custodian.adapters.gradle.updates.usecases.ports;

import eu.xenit.custodian.ports.spi.buildsystem.BuildModification;

public interface GradleBuildUpdateProposal {

    String getDescription();

    BuildModification apply();
}
