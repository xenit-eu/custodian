package eu.xenit.custodian.ports.api;

import java.nio.file.Path;

public interface ProjectHandle {

    ProjectReference getReference();
    Path getLocation();
}
