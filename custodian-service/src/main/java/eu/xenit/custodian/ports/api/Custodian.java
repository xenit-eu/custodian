package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.domain.ProjectUpdateResult;
import eu.xenit.custodian.domain.changes.ChangeSets;
import eu.xenit.custodian.domain.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.project.ProjectHandle;
import eu.xenit.custodian.domain.project.ProjectReference;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.io.IOException;

public interface Custodian {

    ProjectReference createReference(String projectReference);

    ProjectHandle checkoutProject(ProjectReference reference) throws IOException;

    ProjectMetadata extractMetadata(ProjectHandle projectHandle) throws MetadataAnalyzerException;

    ChangeSets getChangeSets(ProjectMetadata metadata);

    // 4. apply selected changes
    ProjectUpdateResult run(ProjectReference projectReference);
}
