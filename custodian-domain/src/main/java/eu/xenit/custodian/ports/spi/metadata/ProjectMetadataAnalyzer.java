package eu.xenit.custodian.ports.spi.metadata;

import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import java.nio.file.Path;

public interface ProjectMetadataAnalyzer {

     void analyze(ClonedRepositorySourceMetadata result, Path location) throws AnalyzerException;

}
