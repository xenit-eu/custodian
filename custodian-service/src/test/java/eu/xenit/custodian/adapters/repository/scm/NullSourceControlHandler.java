package eu.xenit.custodian.adapters.repository.scm;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import eu.xenit.custodian.domain.repository.scm.SourceControlHandler;

public class NullSourceControlHandler implements SourceControlHandler {

    @Override
    public boolean canHandle(ProjectReference reference) {
        return false;
    }

    @Override
    public ProjectHandle checkout(ProjectReference reference) {
        return null;
    }
}
