package eu.xenit.custodian.sentinel.adapters.dependencies;

import eu.xenit.custodian.sentinel.adapters.repositories.JsonRepositoriesReporter;
import eu.xenit.custodian.sentinel.adapters.repositories.RepositoriesAnalyzer;
import eu.xenit.custodian.sentinel.adapters.repositories.RepositoriesContainer;
import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultAnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultReportingFormattingFactory;
import eu.xenit.custodian.sentinel.domain.PartialAnalyzer;
import eu.xenit.custodian.sentinel.domain.ReporterFormattingFactory;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisContributor;
import org.gradle.api.Project;

public class ConfigurationsAnalysisContributor implements SentinelAnalysisContributor<ConfigurationContainer> {

    public static final String NAME = "configurations";

    private final PartialAnalyzer<ConfigurationContainer> analyzer;
    private final ReporterFormattingFactory<ConfigurationContainer> reporting;

    public ConfigurationsAnalysisContributor() {
        this(new ConfigurationsAnalyzer());
    }

    public ConfigurationsAnalysisContributor(PartialAnalyzer<ConfigurationContainer> analyzer)
    {
        this(analyzer, new DefaultReportingFormattingFactory<>(JsonConfigurationsReporter::new));
    }

    public ConfigurationsAnalysisContributor(PartialAnalyzer<ConfigurationContainer> analyzer,
            ReporterFormattingFactory<ConfigurationContainer> reporting) {
        this.analyzer = analyzer;
        this.reporting = reporting;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AnalysisContribution<ConfigurationContainer> analyze(Project project) {
        return new DefaultAnalysisContribution<>(this.analyzer.analyze(project), this.reporting);
    }

}
