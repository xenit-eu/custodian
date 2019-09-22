package eu.xenit.custodian.domain.service.scm;

import eu.xenit.custodian.domain.repository.scm.ProjectHandle;
import eu.xenit.custodian.domain.repository.scm.ProjectReference;
import java.io.IOException;

public interface SourceControlManagementService {

    ProjectHandle checkout(ProjectReference reference) throws IOException;

}
