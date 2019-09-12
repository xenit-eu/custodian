package eu.xenit.custodian.sentinel.reporting;

import eu.xenit.custodian.sentinel.analyzer.SentinelAnalysisResult;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import java.io.IOException;

public interface SentinelReporter {

    void write(IndentingWriter writer, SentinelAnalysisResult result) throws IOException;

}

