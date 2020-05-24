package eu.xenit.custodian.domain.usecases.analysis.ports;

import eu.xenit.custodian.domain.entities.buildsystem.BuildSystemsCollection;

public interface ProjectModel {

    BuildSystemsCollection buildsystems();

}
