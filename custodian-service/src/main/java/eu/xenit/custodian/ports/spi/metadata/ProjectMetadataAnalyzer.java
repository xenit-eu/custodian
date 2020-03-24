package eu.xenit.custodian.ports.spi.metadata;

import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;

public interface ProjectMetadataAnalyzer {

     void analyze(ClonedRepositorySourceMetadata result, ClonedRepositoryHandle handle) throws MetadataAnalyzerException;

}
