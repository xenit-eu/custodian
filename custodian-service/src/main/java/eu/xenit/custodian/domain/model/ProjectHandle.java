package eu.xenit.custodian.domain.model;

import java.nio.file.Path;

public interface ProjectHandle {

    ProjectReference getReference();
    Path getLocation();
}
