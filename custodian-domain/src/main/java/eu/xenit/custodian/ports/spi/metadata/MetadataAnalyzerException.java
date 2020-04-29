package eu.xenit.custodian.ports.spi.metadata;

public class MetadataAnalyzerException extends Exception {

    public MetadataAnalyzerException(String message) {
        super(message);
    }

    public MetadataAnalyzerException(Throwable throwable) {
        super(throwable);
    }

    public MetadataAnalyzerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
