package eu.xenit.custodian.adapters.metadata.gradle;

import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzerFactory;

public class SentinelGradleProjectAnalayzerFactory implements ProjectMetadataAnalyzerFactory {

    @Override
    public ProjectMetadataAnalyzer create() {
        return new SentinelGradleProjectAnalyzer();
    }
}
