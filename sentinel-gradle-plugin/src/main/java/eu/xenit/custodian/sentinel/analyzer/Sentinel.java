package eu.xenit.custodian.sentinel.analyzer;

import eu.xenit.custodian.sentinel.analyzer.dependencies.ConfigurationsAnalyzer;
import eu.xenit.custodian.sentinel.analyzer.gradle.GradleAnalyzer;
import eu.xenit.custodian.sentinel.analyzer.project.ProjectInfoAnalyzer;
import eu.xenit.custodian.sentinel.analyzer.repositories.RepositoriesAnalyzer;
import org.gradle.api.Project;

public class Sentinel {

    private ProjectInfoAnalyzer projectInfoAnalyzer = new ProjectInfoAnalyzer();
    private GradleAnalyzer gradleAnalyzer = new GradleAnalyzer();
    private ConfigurationsAnalyzer configurationsAnalyzer = new ConfigurationsAnalyzer();
    private RepositoriesAnalyzer repositoriesAnalyzer = new RepositoriesAnalyzer();


    public SentinelAnalysisResult analyze(Project project) {

        return SentinelAnalysisResult.builder()
                .project(projectInfoAnalyzer.analyze(project))
                .gradle(gradleAnalyzer.analyze(project))
                .configurations(configurationsAnalyzer.analyze(project))
                .repositories(repositoriesAnalyzer.analyze(project))
                .build();
    }
}
