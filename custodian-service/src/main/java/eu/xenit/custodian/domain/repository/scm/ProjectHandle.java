package eu.xenit.custodian.domain.repository.scm;

import java.nio.file.Path;

public interface ProjectHandle {

    ProjectReference getReference();
    Path getLocation();
}
