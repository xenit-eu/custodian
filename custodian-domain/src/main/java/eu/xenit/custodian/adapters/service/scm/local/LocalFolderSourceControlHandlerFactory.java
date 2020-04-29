package eu.xenit.custodian.adapters.service.scm.local;

import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandlerFactory;

public class LocalFolderSourceControlHandlerFactory implements SourceControlHandlerFactory {

    @Override
    public SourceControlHandler createSourceControlHandler() {
        return new LocalFolderSourceControlHandler();
    }
}
