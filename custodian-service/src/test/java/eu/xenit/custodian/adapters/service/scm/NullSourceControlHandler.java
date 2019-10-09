package eu.xenit.custodian.adapters.service.scm;

import eu.xenit.custodian.domain.project.ProjectHandle;
import eu.xenit.custodian.domain.project.ProjectReference;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;

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
