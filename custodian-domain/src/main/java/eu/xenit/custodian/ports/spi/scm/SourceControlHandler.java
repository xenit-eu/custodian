package eu.xenit.custodian.ports.spi.scm;

import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import java.io.IOException;

public interface SourceControlHandler /* extends Ordered */ {

    boolean canHandle(SourceRepositoryReference reference);

    ClonedRepositoryHandle checkout(SourceRepositoryReference reference) throws IOException;

//    @Override
//    default int getOrder() {
//        return 0;
//    }

}
