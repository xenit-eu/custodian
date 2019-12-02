package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultAnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultReportingFormattingFactory;
import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import eu.xenit.custodian.sentinel.domain.ReporterFormattingFactory;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisContributor;
import org.gradle.api.Project;

public class DependenciesAnalysisContributor implements SentinelAnalysisContributor<DependenciesContainer> {

    public static final String NAME = "dependencies";

    private final PartialAnalyzer<DependenciesContainer> analyzer;

    public DependenciesAnalysisContributor() {
        this(new DependenciesAnalyzer());
    }

    public DependenciesAnalysisContributor(PartialAnalyzer<DependenciesContainer> analyzer)
    {
        this(analyzer, new DefaultReportingFormattingFactory<>(JsonDependenciesReporter::new));
    }

    public DependenciesAnalysisContributor(PartialAnalyzer<DependenciesContainer> analyzer,
            ReporterFormattingFactory<DependenciesContainer> reporting) {
        this.analyzer = analyzer;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AnalysisContribution<DependenciesContainer> analyze(Project project) {
        return new DependenciesContribution(this.analyzer.analyze(project));
    }

}
