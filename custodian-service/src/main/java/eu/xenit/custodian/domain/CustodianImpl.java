package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.project.ProjectReferenceParser;
import eu.xenit.custodian.domain.changes.LogicalChangeSet;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.ports.api.ProjectUpdateResult;
import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.io.IOException;

public class CustodianImpl implements Custodian {


    private final SourceControlHandler scmHandler;
    private final ProjectMetadataAnalyzer analyzer;
    private final UpdateChannel channel;

    CustodianImpl(SourceControlHandler scm, ProjectMetadataAnalyzer analyzer, UpdateChannel channel) {

        this.scmHandler = scm;
        this.analyzer = analyzer;
        this.channel = channel;
    }

    @Override
    public SourceRepositoryReference createReference(String repositoryReference) {
        return ProjectReferenceParser.from(repositoryReference);
    }

    @Override
    public ClonedRepositoryHandle checkoutProject(SourceRepositoryReference reference) throws IOException {
        return this.scmHandler.checkout(reference);
    }

    @Override
    public ClonedRepositorySourceMetadata extractMetadata(ClonedRepositoryHandle clonedRepositoryHandle) throws MetadataAnalyzerException {
        ClonedRepositorySourceMetadata result = new ClonedRepositorySourceMetadataAnalysisResult(clonedRepositoryHandle);
        this.analyzer.analyze(result, clonedRepositoryHandle);
        return result;
    }

    @Override
    public LogicalChangeSet getChanges(ClonedRepositorySourceMetadata metadata) {
        return new DefaultLogicalChangeSet(this.channel.getChanges(metadata));
    }

    @Override
    public ProjectUpdateResult run(SourceRepositoryReference sourceRepositoryReference) {
        return new ProjectUpdateResult() {
        };
    }
}
