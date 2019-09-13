package eu.xenit.custodian.sentinel.adapters.repositories;

import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultAnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultReportingFormattingFactory;
import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import eu.xenit.custodian.sentinel.domain.ReporterFormattingFactory;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisContributor;
import org.gradle.api.Project;

public class RepositoriesAnalysisContributor implements SentinelAnalysisContributor<RepositoriesContainer> {

    public static final String NAME = "repositories";

    private final PartialAnalyzer<RepositoriesContainer> analyzer;
    private final ReporterFormattingFactory<RepositoriesContainer> reporting;

    public RepositoriesAnalysisContributor() {
        this(new RepositoriesAnalyzer(), new DefaultReportingFormattingFactory<>(JsonRepositoriesReporter::new));
    }

    public RepositoriesAnalysisContributor(PartialAnalyzer<RepositoriesContainer> analyzer, ReporterFormattingFactory<RepositoriesContainer> reporting)
    {
        this.analyzer = analyzer;
        this.reporting = reporting;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AnalysisContribution<RepositoriesContainer> analyze(Project project) {
        return new DefaultAnalysisContribution<>(this.analyzer.analyze(project), this.reporting);
    }

}
