package eu.xenit.custodian.domain.repository.analysis;

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
