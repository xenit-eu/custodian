package eu.xenit.custodian.sentinel.domain;

import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;

public interface AnalysisContribution<T extends AnalysisContentPart> {

    T getContent();
    PartialReporter<T> createReporter(OutputFormat format);

    default Runnable report(IndentingWriter writer, OutputFormat format) {
        return this.createReporter(format).report(writer, this.getContent());
    }
}
