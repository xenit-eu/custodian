package eu.xenit.custodian.sentinel.domain;

import org.gradle.api.Project;

@FunctionalInterface
public interface PartialAnalyzer<TResult extends AnalysisContentPart> {

    TResult analyze(Project project);

}
