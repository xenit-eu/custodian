package eu.xenit.custodian.sentinel.domain;

import eu.xenit.custodian.sentinel.adapters.dependencies.ConfigurationsAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.gradle.GradleAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.project.ProjectInfoAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.repositories.RepositoriesAnalysisContributor;
import java.util.Arrays;
import java.util.Collection;
import org.gradle.api.Project;

public class Sentinel {
//
//    private AnalyzerContainer analyzers = new AnalyzerContainer();

    private AnalysisContributorContainer contributors = new AnalysisContributorContainer();
//    private ProjectInfoAnalyzer projectInfoAnalyzer = new ProjectInfoAnalyzer();
//    private GradleAnalyzer gradleAnalyzer = new GradleAnalyzer();
//    private ConfigurationsAnalyzer configurationsAnalyzer = new ConfigurationsAnalyzer();
//    private RepositoriesAnalyzer repositoriesAnalyzer = new RepositoriesAnalyzer();

    // does not comply with hexagonal architecture - provide adapters as arguments!
    @Deprecated
    public Sentinel() {
        this(Arrays.asList(
                new GradleAnalysisContributor(),
                new ProjectInfoAnalysisContributor(),
                new RepositoriesAnalysisContributor(),
                new ConfigurationsAnalysisContributor()
        ));
    }

    public Sentinel(Collection<SentinelAnalysisContributor> contributors) {
        contributors.forEach(this.contributors::add);
    }

    public SentinelAnalysisReport analyze(Project project) {

        SentinelAnalysisReport report = new SentinelAnalysisReport();

        this.contributors.items().forEach(contributor -> {
            AnalysisContribution contribution = contributor.analyze(project);
            report.add(contributor.getName(), contribution);
        });

        return report;
    }
}
