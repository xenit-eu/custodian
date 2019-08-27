package eu.xenit.custodian.sentinel.reporting;

import eu.xenit.custodian.sentinel.analyzer.model.SentinelAnalysisResult;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;

public interface SentinelReporter {

    void write(IndentingWriter writer, SentinelAnalysisResult result);

}

