package eu.xenit.custodian.domain;

import eu.xenit.custodian.ports.api.ProjectMetadata;
import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.ports.api.ProjectHandle;
import eu.xenit.custodian.ports.api.ProjectReference;
import org.springframework.util.Assert;

public class ProjectMetadataAnalysisResult implements ProjectMetadata {

    private final ProjectHandle projectHandle;

    private BuildSystemsContainer buildSystems = new BuildSystemsContainer();

    ProjectMetadataAnalysisResult(ProjectHandle handle)
    {
        Assert.notNull(handle, "handle must not be null");
        this.projectHandle = handle;
    }

    @Override
    public ProjectReference getReference() {
        return this.projectHandle.getReference();
    }

    @Override
    public BuildSystemsContainer buildsystems() {
        return buildSystems;
    }





}
