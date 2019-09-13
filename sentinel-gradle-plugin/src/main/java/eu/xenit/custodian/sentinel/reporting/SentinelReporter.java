package eu.xenit.custodian.sentinel.reporting;

import eu.xenit.custodian.sentinel.domain.SentinelAnalysisReport;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import java.io.IOException;

public interface SentinelReporter {

    void write(IndentingWriter writer, SentinelAnalysisReport result) throws IOException;

}

