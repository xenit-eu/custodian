package eu.xenit.custodian.ports.spi.metadata;

import eu.xenit.custodian.ports.api.ProjectMetadata;
import eu.xenit.custodian.ports.api.ProjectHandle;

public interface ProjectMetadataAnalyzer {

     void analyze(ProjectMetadata result, ProjectHandle handle) throws MetadataAnalyzerException;

}
