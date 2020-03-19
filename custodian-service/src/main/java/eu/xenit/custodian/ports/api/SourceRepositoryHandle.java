package eu.xenit.custodian.ports.api;

import java.nio.file.Path;

public interface SourceRepositoryHandle {

    SourceRepositoryReference getReference();
    Path getLocation();
}
