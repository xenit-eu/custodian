package eu.xenit.custodian.sentinel.adapters.project;

import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultAnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultReportingFormattingFactory;
import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import eu.xenit.custodian.sentinel.domain.ReporterFormattingFactory;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisContributor;
import org.gradle.api.Project;

public class ProjectInfoAnalysisContributor implements SentinelAnalysisContributor<ProjectInformation> {

    public static final String NAME = "project";

    private final PartialAnalyzer<ProjectInformation> analyzer;
    private final ReporterFormattingFactory<ProjectInformation> reporting;

    public ProjectInfoAnalysisContributor() {
        this(new ProjectInfoAnalyzer(true));
    }

    public ProjectInfoAnalysisContributor(PartialAnalyzer<ProjectInformation> analyzer) {
        this(analyzer, new DefaultReportingFormattingFactory<>(ProjectInfoJsonReporter::new));
    }

    public ProjectInfoAnalysisContributor(PartialAnalyzer<ProjectInformation> analyzer, ReporterFormattingFactory<ProjectInformation> reporting)
    {
        this.analyzer = analyzer;
        this.reporting = reporting;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AnalysisContribution<ProjectInformation> analyze(Project project) {
        return new DefaultAnalysisContribution<>(this.analyzer.analyze(project), this.reporting);
    }

}
