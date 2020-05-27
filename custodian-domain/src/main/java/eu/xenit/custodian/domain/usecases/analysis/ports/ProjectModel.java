package eu.xenit.custodian.domain.usecases.analysis.ports;

import eu.xenit.custodian.domain.entities.buildsystem.BuildSystemsCollection;

public interface ProjectModel {

    /**
     * A collection of {@link eu.xenit.custodian.ports.spi.buildsystem.BuildSystem}s in this {@link ProjectModel}.
     *
     * Never null
     */
    BuildSystemsCollection buildsystems();

}
