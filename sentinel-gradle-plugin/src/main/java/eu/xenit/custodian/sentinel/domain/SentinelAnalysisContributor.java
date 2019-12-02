package eu.xenit.custodian.sentinel.domain;

import org.gradle.api.Project;

/**
 * Service Provider Interface for contributing to the Gralde build analysis.
 *
 * Components that want to analyse an aspect of the Gradle build, should implement this interface
 *
 * @param <TResult>
 */
public interface SentinelAnalysisContributor<TResult extends AnalysisContentPart> {

    AnalysisContribution<TResult> analyze(Project project);

    String getName();
}
