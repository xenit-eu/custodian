package eu.xenit.custodian.adapters.service.scm;

import eu.xenit.custodian.ports.api.SourceRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;

public class NullSourceControlHandler implements SourceControlHandler {

    @Override
    public boolean canHandle(SourceRepositoryReference reference) {
        return false;
    }

    @Override
    public SourceRepositoryHandle checkout(SourceRepositoryReference reference) {
        return null;
    }
}
