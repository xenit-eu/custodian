package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.metadata.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.domain.project.ProjectReference;

public interface ProjectMetadata {

    ProjectReference getReference();

    BuildSystemsContainer buildsystems();
}
