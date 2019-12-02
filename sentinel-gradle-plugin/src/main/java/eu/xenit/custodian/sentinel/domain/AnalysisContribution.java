package eu.xenit.custodian.sentinel.domain;

import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;

/**
 *
 * @param <T>
 */
public interface AnalysisContribution<T extends AnalysisContentPart> {

    String getName();

    T getAnalysis();

    PartialReporter<T> getReporter(OutputFormat format);

    default Runnable report(IndentingWriter writer, OutputFormat format) {
        return this.getReporter(format).report(writer, this.getAnalysis());
    }
}
