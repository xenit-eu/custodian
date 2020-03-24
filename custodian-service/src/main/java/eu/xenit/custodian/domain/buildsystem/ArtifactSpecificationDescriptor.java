package eu.xenit.custodian.domain.buildsystem;

import java.util.Set;

public interface ArtifactSpecificationDescriptor<T extends ArtifactSpecification> {

    Set<T> getArtifactSpecs();

}
