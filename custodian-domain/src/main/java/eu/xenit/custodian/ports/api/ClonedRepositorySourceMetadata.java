package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;

public interface ClonedRepositorySourceMetadata {

    SourceRepositoryReference getReference();

    BuildSystemsContainer buildsystems();
}
