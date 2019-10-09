package eu.xenit.custodian.ports.spi.scm;

import eu.xenit.custodian.domain.project.ProjectHandle;
import eu.xenit.custodian.domain.project.ProjectReference;
import java.io.IOException;
import org.springframework.core.Ordered;

public interface SourceControlHandler extends Ordered {

    boolean canHandle(ProjectReference reference);

    ProjectHandle checkout(ProjectReference reference) throws IOException;

    @Override
    default int getOrder() {
        return 0;
    }
}
