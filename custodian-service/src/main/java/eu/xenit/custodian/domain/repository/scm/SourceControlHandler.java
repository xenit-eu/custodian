package eu.xenit.custodian.domain.repository.scm;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import java.io.IOException;

public interface SourceControlHandler {

    boolean canHandle(ProjectReference reference);

    ProjectHandle checkout(ProjectReference reference) throws IOException;
}
