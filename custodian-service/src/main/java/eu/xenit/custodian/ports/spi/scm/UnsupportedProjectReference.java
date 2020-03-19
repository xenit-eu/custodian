package eu.xenit.custodian.ports.spi.scm;

import eu.xenit.custodian.ports.api.SourceRepositoryReference;

public class UnsupportedProjectReference extends RuntimeException {

    private final SourceRepositoryReference sourceRepositoryReference;

    public UnsupportedProjectReference(String message, SourceRepositoryReference sourceRepositoryReference)
    {
        super(message);
        this.sourceRepositoryReference = sourceRepositoryReference;
    }

    public SourceRepositoryReference getSourceRepositoryReference() {
        return this.sourceRepositoryReference;
    }

}
