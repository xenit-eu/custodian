package eu.xenit.custodian.sentinel.domain;

public class DefaultAnalysisContribution<T extends AnalysisContentPart> implements AnalysisContribution<T> {

    private final T content;
    private final ReporterFormattingFactory<T> reporterFactory;

    public DefaultAnalysisContribution(T content, ReporterFormattingFactory<T> reporting) {
        this.content = content;
        this.reporterFactory = reporting;
    }

    public T getContent() { return this.content; }
    public PartialReporter<T> createReporter(OutputFormat format) {
        return this.reporterFactory.createReporter(format);
    }

}
