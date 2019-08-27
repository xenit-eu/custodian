package eu.xenit.gradle.sentinel.analyzer;

import eu.xenit.gradle.sentinel.analyzer.model.SentinelAnalysisResult;
import org.gradle.api.Project;

public class SentinelProjectAnalyzer {

    private ConfigurationsAnalyzer configurationsAnalyzer = new ConfigurationsAnalyzer();
    private RepositoriesAnalyzer repositoriesAnalyzer = new RepositoriesAnalyzer();

    public SentinelAnalysisResult analyze(Project project) {

        return new SentinelAnalysisResult(
                configurationsAnalyzer.analyze(project),
                repositoriesAnalyzer.analyze(project)
        );

    }
}
