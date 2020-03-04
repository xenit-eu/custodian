package eu.xenit.custodian.domain.metadata;

import eu.xenit.custodian.ports.api.ProjectHandle;
import eu.xenit.custodian.ports.api.ProjectMetadata;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public class CompositeProjectMetadataAnalyzer implements ProjectMetadataAnalyzer {

    private final Collection<ProjectMetadataAnalyzer> analyzers;

    public CompositeProjectMetadataAnalyzer(Collection<ProjectMetadataAnalyzer> analyzers) {
        Objects.requireNonNull(analyzers, "analyzers must not be null");
        this.analyzers = analyzers;
    }

    @Override
    public void analyze(ProjectMetadata result, ProjectHandle handle) throws MetadataAnalyzerException {
        for (ProjectMetadataAnalyzer analyzer : this.analyzers) {
            analyzer.analyze(result, handle);
        }
    }

    public int size() {
        return this.analyzers.size();
    }

    public Stream<ProjectMetadataAnalyzer> analyzers() {
        return this.analyzers.stream();
    }
}
