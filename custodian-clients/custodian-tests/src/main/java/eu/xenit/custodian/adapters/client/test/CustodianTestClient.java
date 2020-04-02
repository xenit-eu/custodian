package eu.xenit.custodian.adapters.client.test;

import eu.xenit.custodian.domain.changes.LogicalChangeSet;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.io.IOException;
import java.nio.file.Path;

public class CustodianTestClient {

    private final Custodian custodian;

    public CustodianTestClient(Custodian custodian) {
        this.custodian = custodian;
    }

    public SourceRepositoryReference createReference(Path location) {
        return this.custodian.createReference(location.toString());
    }

    public ClonedRepositoryHandle checkout(SourceRepositoryReference reference) throws IOException {
        return this.custodian.checkoutProject(reference);
    }

    public ClonedRepositoryHandle checkout(Path location) throws IOException {
        return this.custodian.checkoutProject(this.createReference(location));
    }

    public ClonedRepositorySourceMetadataAssertionTrait extractMetadata(ClonedRepositoryHandle handle) throws MetadataAnalyzerException {
        ClonedRepositorySourceMetadata clonedRepositorySourceMetadata = this.custodian.extractMetadata(handle);
        return new ClonedRepositorySourceMetadataAssertionTrait(clonedRepositorySourceMetadata);
    }

    public ChangeSetAssertionTrait getChanges(ClonedRepositorySourceMetadata metadata) {
        LogicalChangeSet changes = this.custodian.getChanges(metadata);
        return new ChangeSetAssertionTrait(changes);
    }


}
