package eu.xenit.custodian.ports.spi.metadata;

import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.api.SourceRepositoryHandle;

public interface ProjectMetadataAnalyzer {

     void analyze(ClonedRepositorySourceMetadata result, SourceRepositoryHandle handle) throws MetadataAnalyzerException;

}
