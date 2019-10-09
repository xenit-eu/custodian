package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.project.ProjectHandle;
import eu.xenit.custodian.domain.project.ProjectReference;
import eu.xenit.custodian.domain.project.ProjectReferenceParser;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.io.IOException;

public class CustodianImpl implements Custodian {


    private final SourceControlHandler scmHandler;
    private final ProjectMetadataAnalyzer analyzer;

    CustodianImpl(SourceControlHandler scm, ProjectMetadataAnalyzer analyzer) {

        this.scmHandler = scm;
        this.analyzer = analyzer;
    }

    @Override
    public ProjectReference createReference(String projectReference){
        return ProjectReferenceParser.from(projectReference);
    }

    @Override
    public ProjectHandle checkoutProject(ProjectReference reference) throws IOException {
        return this.scmHandler.checkout(reference);
    }

    @Override
    public ProjectMetadata extractMetadata(ProjectHandle projectHandle) throws MetadataAnalyzerException {
        ProjectMetadata result = new ProjectMetadataAnalysisResult(projectHandle);
        this.analyzer.analyze(result, projectHandle);
        return result;
    }

    @Override
    public ProjectUpdateResult run(ProjectReference projectReference) {
        return new ProjectUpdateResult();
    }
}
