package eu.xenit.custodian.sentinel.domain;

import org.gradle.api.Project;

public interface SentinelAnalysisContributor<TResult extends AnalysisContentPart> {

    AnalysisContribution<TResult> analyze(Project project);

    String getName();
}
