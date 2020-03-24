package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.domain.changes.LogicalChangeSet;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.io.IOException;

public interface Custodian {

    SourceRepositoryReference createReference(String repositoryReference);

    ClonedRepositoryHandle checkoutProject(SourceRepositoryReference reference) throws IOException;

    ClonedRepositorySourceMetadata extractMetadata(ClonedRepositoryHandle clonedRepositoryHandle) throws MetadataAnalyzerException;

    LogicalChangeSet getChanges(ClonedRepositorySourceMetadata metadata);

    // 4. apply selected changes
    ProjectUpdateResult run(SourceRepositoryReference sourceRepositoryReference);
}
