package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.asserts.build.buildsystem.BuildSystemsContainer;

public interface ClonedRepositorySourceMetadata {

    SourceRepositoryReference getReference();

    BuildSystemsContainer buildsystems();
}
