package eu.xenit.custodian.adapters.client.test;

import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.domain.metadata.ClonedRepositorySourceMetadataAssert;
import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import java.util.Objects;
import org.assertj.core.api.AssertProvider;

public class ClonedRepositorySourceMetadataAssertionTrait implements ClonedRepositorySourceMetadata, AssertProvider<ClonedRepositorySourceMetadataAssert> {

    private final ClonedRepositorySourceMetadata clonedRepositorySourceMetadata;

    ClonedRepositorySourceMetadataAssertionTrait(ClonedRepositorySourceMetadata clonedRepositorySourceMetadata) {
        this.clonedRepositorySourceMetadata = clonedRepositorySourceMetadata;
    }

    @Override
    public SourceRepositoryReference getReference() {
        Objects.requireNonNull(this.clonedRepositorySourceMetadata, "projectMetadata is null");
        return this.clonedRepositorySourceMetadata.getReference();
    }

    @Override
    public BuildSystemsContainer buildsystems() {
        Objects.requireNonNull(this.clonedRepositorySourceMetadata, "projectMetadata is null");
        return this.clonedRepositorySourceMetadata.buildsystems();
    }

    @Override
    public ClonedRepositorySourceMetadataAssert assertThat() {
        return new ClonedRepositorySourceMetadataAssert(this.clonedRepositorySourceMetadata);
    }
}
