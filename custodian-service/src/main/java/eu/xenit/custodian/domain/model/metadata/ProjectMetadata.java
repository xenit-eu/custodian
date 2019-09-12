package eu.xenit.custodian.domain.model.metadata;

import eu.xenit.custodian.domain.model.ProjectReference;
import eu.xenit.custodian.domain.model.buildsystem.BuildSystemsContainer;
import org.springframework.util.Assert;

public class ProjectMetadata {

    private final ProjectReference reference;

    private BuildSystemsContainer buildSystems = new BuildSystemsContainer();

    public ProjectMetadata(ProjectReference reference)
    {
        Assert.notNull(reference, "reference must not be null");
        this.reference = reference;
    }

    public ProjectReference getReference() {
        return this.reference;
    }

    public BuildSystemsContainer buildsystems() {
        return buildSystems;
    }





}
