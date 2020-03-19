package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.asserts.build.changes.LogicalChangeSet;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.io.IOException;

public interface Custodian {

    SourceRepositoryReference createReference(String repositoryReference);

    SourceRepositoryHandle checkoutProject(SourceRepositoryReference reference) throws IOException;

    ClonedRepositorySourceMetadata extractMetadata(SourceRepositoryHandle sourceRepositoryHandle) throws MetadataAnalyzerException;

    LogicalChangeSet getChanges(ClonedRepositorySourceMetadata metadata);

    // 4. apply selected changes
    ProjectUpdateResult run(SourceRepositoryReference sourceRepositoryReference);
}
