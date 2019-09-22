package eu.xenit.custodian.domain.repository.scm;

import java.io.IOException;

public interface SourceControlHandler {

    boolean canHandle(ProjectReference reference);

    ProjectHandle checkout(ProjectReference reference) throws IOException;
}
