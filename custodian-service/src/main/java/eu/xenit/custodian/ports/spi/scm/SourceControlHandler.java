package eu.xenit.custodian.ports.spi.scm;

import eu.xenit.custodian.ports.api.SourceRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import java.io.IOException;
import org.springframework.core.Ordered;

public interface SourceControlHandler extends Ordered {

    boolean canHandle(SourceRepositoryReference reference);

    SourceRepositoryHandle checkout(SourceRepositoryReference reference) throws IOException;

    @Override
    default int getOrder() {
        return 0;
    }
}
