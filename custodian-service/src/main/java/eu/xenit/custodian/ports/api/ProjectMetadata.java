package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.ports.api.ProjectReference;

public interface ProjectMetadata {

    ProjectReference getReference();

    BuildSystemsContainer buildsystems();
}
