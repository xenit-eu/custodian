package eu.xenit.custodian.sentinel.domain;

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

    public Sentinel(Collection<SentinelAnalysisContributor<? extends AnalysisContentPart>> contributors) {
        contributors.forEach(c -> this.contributors.add(c));
    }

    public SentinelAnalysisReport analyze(Project project) {

        SentinelAnalysisReport report = new SentinelAnalysisReport();

        this.contributors.items().forEach(contributor -> {
            AnalysisContribution<? extends AnalysisContentPart> contribution = contributor.analyze(project);
            report.add(contributor.getName(), contribution);
        });

        return report;
    }
}
