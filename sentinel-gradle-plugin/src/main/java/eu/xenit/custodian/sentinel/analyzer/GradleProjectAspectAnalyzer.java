package eu.xenit.custodian.sentinel.analyzer;

import org.gradle.api.Project;

public interface GradleProjectAspectAnalyzer<TResult extends AspectAnalysis> {

    TResult analyze(Project project);
}
