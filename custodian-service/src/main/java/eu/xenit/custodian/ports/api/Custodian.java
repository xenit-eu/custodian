package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.domain.changes.LogicalChangeSet;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.io.IOException;

public interface Custodian {

    ProjectReference createReference(String projectReference);

    ProjectHandle checkoutProject(ProjectReference reference) throws IOException;

    ProjectMetadata extractMetadata(ProjectHandle projectHandle) throws MetadataAnalyzerException;

    LogicalChangeSet getChanges(ProjectMetadata metadata);

    // 4. apply selected changes
    ProjectUpdateResult run(ProjectReference projectReference);
}
