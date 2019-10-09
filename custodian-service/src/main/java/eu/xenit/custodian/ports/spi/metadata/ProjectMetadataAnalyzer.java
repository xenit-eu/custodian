package eu.xenit.custodian.ports.spi.metadata;

import eu.xenit.custodian.domain.ProjectMetadata;
import eu.xenit.custodian.domain.ProjectMetadataAnalysisResult;
import eu.xenit.custodian.domain.project.ProjectHandle;

public interface ProjectMetadataAnalyzer {

     void analyze(ProjectMetadata result, ProjectHandle handle) throws MetadataAnalyzerException;

}
