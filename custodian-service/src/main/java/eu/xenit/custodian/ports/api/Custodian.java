package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.domain.ProjectMetadata;
import eu.xenit.custodian.domain.ProjectMetadataAnalysisResult;
import eu.xenit.custodian.domain.project.ProjectHandle;
import eu.xenit.custodian.domain.project.ProjectReference;
import eu.xenit.custodian.domain.ProjectUpdateResult;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.io.IOException;

public interface Custodian {

    ProjectReference createReference(String projectReference);

    ProjectHandle checkoutProject(ProjectReference reference) throws IOException;

    ProjectMetadata extractMetadata(ProjectHandle projectHandle) throws MetadataAnalyzerException;

    // 2. look for updates via "channels"
    // DependencyUpdates checkForUpdates(ProjectMetadata)

    // 3. gather change-sets

    // 4. apply selected changes
    ProjectUpdateResult run(ProjectReference projectReference);
}
