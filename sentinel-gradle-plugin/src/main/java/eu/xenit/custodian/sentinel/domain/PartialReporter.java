package eu.xenit.custodian.sentinel.domain;

import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;

public interface PartialReporter<TResult extends AnalysisContentPart> {

    Runnable report(IndentingWriter writer, TResult analysis);
}
