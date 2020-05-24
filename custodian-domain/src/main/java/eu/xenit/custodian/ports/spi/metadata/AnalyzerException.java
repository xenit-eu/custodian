package eu.xenit.custodian.ports.spi.metadata;

public class AnalyzerException extends Exception {

    public AnalyzerException(String message) {
        super(message);
    }

    public AnalyzerException(Throwable throwable) {
        super(throwable);
    }

    public AnalyzerException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
