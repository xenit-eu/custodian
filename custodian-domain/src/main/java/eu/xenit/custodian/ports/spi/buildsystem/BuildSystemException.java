package eu.xenit.custodian.ports.spi.buildsystem;

public class BuildSystemException extends Exception {

    public BuildSystemException(String message) {
        super(message);
    }

    public BuildSystemException(Exception innerEx) {
        super(innerEx);
    }
}
