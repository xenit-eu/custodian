package eu.xenit.custodian.sentinel.domain;

@FunctionalInterface
public interface ReporterFormattingFactory<T extends AnalysisContentPart> {

    PartialReporter<T> createReporter(OutputFormat format);
}
