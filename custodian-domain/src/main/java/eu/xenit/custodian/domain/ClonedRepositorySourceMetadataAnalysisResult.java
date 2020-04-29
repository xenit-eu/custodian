package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import org.springframework.util.Assert;

public class ClonedRepositorySourceMetadataAnalysisResult implements ClonedRepositorySourceMetadata {

    private final ClonedRepositoryHandle clonedRepositoryHandle;

    private BuildSystemsContainer buildSystems = new BuildSystemsContainer();

    ClonedRepositorySourceMetadataAnalysisResult(ClonedRepositoryHandle handle)
    {
        Assert.notNull(handle, "handle must not be null");
        this.clonedRepositoryHandle = handle;
    }

    @Override
    public SourceRepositoryReference getReference() {
        return this.clonedRepositoryHandle.getReference();
    }

    @Override
    public BuildSystemsContainer buildsystems() {
        return buildSystems;
    }





}
