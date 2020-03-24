package eu.xenit.custodian.ports.api;

import java.nio.file.Path;

public interface ClonedRepositoryHandle {

    SourceRepositoryReference getReference();
    Path getLocation();
}
