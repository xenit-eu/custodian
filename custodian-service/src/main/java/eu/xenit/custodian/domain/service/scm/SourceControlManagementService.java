package eu.xenit.custodian.domain.service.scm;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import java.io.IOException;

public interface SourceControlManagementService {

    ProjectHandle checkout(ProjectReference reference) throws IOException;

}
