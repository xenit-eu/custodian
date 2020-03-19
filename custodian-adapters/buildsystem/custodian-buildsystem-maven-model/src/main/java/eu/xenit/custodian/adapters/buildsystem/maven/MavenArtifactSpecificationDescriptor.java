package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.asserts.build.buildsystem.ArtifactSpecificationDescriptor;
import eu.xenit.custodian.asserts.build.buildsystem.ModuleDependency;
import java.util.Set;

public interface MavenArtifactSpecificationDescriptor extends
        ArtifactSpecificationDescriptor<MavenArtifactSpecification> {


    MavenModuleIdentifier getModuleId();
    MavenVersionSpecification getVersionSpec();

    ModuleDependency getDependency();

    // Unlike Maven, a single (maven-)dependency in Gradle can have multiple artifacts
    // To correctly model this, we need to account that we can have a set of artifact-specs
    Set<MavenArtifactSpecification> getArtifactSpecs();

}
