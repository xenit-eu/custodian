package eu.xenit.gradle.sentinel.reporting;

import eu.xenit.gradle.sentinel.analyzer.model.SentinelAnalysisResult;
import eu.xenit.gradle.sentinel.reporting.io.IndentingWriter;

public interface SentinelReporter {

    void write(IndentingWriter writer, SentinelAnalysisResult result);

}

