package eu.xenit.custodian.domain.metadata;

import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.domain.project.ProjectReference;

public interface ProjectMetadata {

    ProjectReference getReference();

    BuildSystemsContainer buildsystems();
}
