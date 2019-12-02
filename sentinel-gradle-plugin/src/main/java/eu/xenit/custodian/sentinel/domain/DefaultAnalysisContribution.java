package eu.xenit.custodian.sentinel.domain;

public class DefaultAnalysisContribution<T extends AnalysisContentPart> implements AnalysisContribution<T> {

    private final T analysis;
    private final ReporterFormattingFactory<T> reporterFactory;

    public DefaultAnalysisContribution(T analysis, ReporterFormattingFactory<T> reporting) {
        this.analysis = analysis;
        this.reporterFactory = reporting;
    }

    @Override
    public String getName() {
        return this.analysis.getContributionName();
    }

    public T getAnalysis() { return this.analysis; }

    public PartialReporter<T> getReporter(OutputFormat format) {
        return this.reporterFactory.createReporter(format);
    }

}
