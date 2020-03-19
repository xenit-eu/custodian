package eu.xenit.custodian.adapters.client.test;

import eu.xenit.custodian.asserts.build.changes.LogicalChangeSet;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.api.SourceRepositoryHandle;
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

    public ClonedRepositorySourceMetadataAssertionTrait extractMetadata(Path location) throws IOException, MetadataAnalyzerException {
        SourceRepositoryReference reference = this.createReference(location);
        SourceRepositoryHandle handle = this.custodian.checkoutProject(reference);
        return this.extractMetadata(handle);

    }

    public ClonedRepositorySourceMetadataAssertionTrait extractMetadata(SourceRepositoryHandle handle) throws MetadataAnalyzerException {
        ClonedRepositorySourceMetadata clonedRepositorySourceMetadata = this.custodian.extractMetadata(handle);
        return new ClonedRepositorySourceMetadataAssertionTrait(clonedRepositorySourceMetadata);
    }

    public ChangeSetAssertionTrait getChanges(ClonedRepositorySourceMetadata metadata) {
        LogicalChangeSet changes = this.custodian.getChanges(metadata);
        return new ChangeSetAssertionTrait(changes);
    }


}
