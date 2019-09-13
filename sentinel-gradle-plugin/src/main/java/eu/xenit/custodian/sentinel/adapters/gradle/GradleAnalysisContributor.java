package eu.xenit.custodian.sentinel.adapters.gradle;

import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultAnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultReportingFormattingFactory;
import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import eu.xenit.custodian.sentinel.domain.ReporterFormattingFactory;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisContributor;
import org.gradle.api.Project;

public class GradleAnalysisContributor implements SentinelAnalysisContributor<GradleInfo> {

    public static final String NAME = "gradle";

    private final PartialAnalyzer<GradleInfo> analyzer;
    private final ReporterFormattingFactory<GradleInfo> reporting;

    public GradleAnalysisContributor()
    {
        this(new GradleAnalyzer(), new DefaultReportingFormattingFactory<>(GradleJsonReporter::new));
    }

    GradleAnalysisContributor(PartialAnalyzer<GradleInfo> analyzer, ReporterFormattingFactory<GradleInfo> reporting)
    {
        this.analyzer = analyzer;
        this.reporting = reporting;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AnalysisContribution<GradleInfo> analyze(Project project) {
        return new DefaultAnalysisContribution<>(this.analyzer.analyze(project), this.reporting);
    }


}
