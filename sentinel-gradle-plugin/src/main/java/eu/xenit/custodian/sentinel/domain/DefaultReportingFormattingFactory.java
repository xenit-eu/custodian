package eu.xenit.custodian.sentinel.domain;

import java.util.function.Supplier;

public class DefaultReportingFormattingFactory<T extends AnalysisContentPart> implements ReporterFormattingFactory<T> {

    private final Supplier<JsonPartialReporter<T>> json;

    // The arguments should be extended when we add new formats
    public DefaultReportingFormattingFactory(Supplier<JsonPartialReporter<T>> json) {

        this.json = json;
    }

    @Override
    public PartialReporter<T> createReporter(OutputFormat format) {
        switch (format) {
            case JSON:
                return this.json.get();
            default:
                String msg = String.format("Format %s is not supported", format.toString());
                throw new UnsupportedOperationException(msg);
        }
    }
}
