package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.ports.spi.buildsystem.RepositoryContainer;
import java.util.stream.Stream;

public class GradleRepositoryContainer extends RepositoryContainer<GradleArtifactRepository> {

    public GradleRepositoryContainer() {

    }

    public GradleRepositoryContainer(Stream<GradleArtifactRepository> stream) {
        super(stream);
    }

}
