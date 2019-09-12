package eu.xenit.custodian.domain.service.analysis;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.repository.analysis.AnalyzerException;
import eu.xenit.custodian.domain.repository.analysis.ProjectAnalyzer;
import org.springframework.util.Assert;

public class DefaultProjectAnalysisService implements ProjectAnalysisService {

    private final ProjectAnalyzer analyzer;

    DefaultProjectAnalysisService(ProjectAnalyzer analyzer) {
        Assert.notNull(analyzer, "analyzer must not be null");
        this.analyzer = analyzer;
    }

    @Override
    public ProjectMetadata analyze(ProjectHandle project) throws AnalyzerException {
        return this.analyzer.analyze(project);
    }
}
