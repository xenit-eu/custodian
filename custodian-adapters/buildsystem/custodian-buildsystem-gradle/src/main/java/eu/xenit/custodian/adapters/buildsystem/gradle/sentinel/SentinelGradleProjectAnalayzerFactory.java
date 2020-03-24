package eu.xenit.custodian.adapters.buildsystem.gradle.sentinel;

import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.SentinelGradleProjectAnalyzer;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzerFactory;

public class SentinelGradleProjectAnalayzerFactory implements ProjectMetadataAnalyzerFactory {

    @Override
    public ProjectMetadataAnalyzer create() {
        return new SentinelGradleProjectAnalyzer();
    }
}
