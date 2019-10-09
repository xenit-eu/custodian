package eu.xenit.custodian.domain.project;

import java.nio.file.Path;

public interface ProjectHandle {

    ProjectReference getReference();
    Path getLocation();
}
