package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationDescriptor;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleIdentifier;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class MavenArtifactSpecificationDescriptorAdaptor implements MavenArtifactSpecificationDescriptor {

    private final GradleModuleDependency dependency;

    MavenArtifactSpecificationDescriptorAdaptor(GradleModuleDependency dependency) {
        Objects.requireNonNull(dependency, "Argument dependency is required");
        this.dependency = dependency;
    }

    @Override
    public MavenModuleIdentifier getModuleId() {
        return MavenModuleIdentifier.from(dependency.getModuleId());
    }

    @Override
    public MavenVersionSpecification getVersionSpec() {
        return MavenVersionSpecification.from(dependency.getVersionSpec());
    }

    @Override
    public GradleModuleDependency getDependency() {
        return this.dependency;
    }

    @Override
    public Set<MavenArtifactSpecification> getArtifactSpecs() {


        return this.dependency.getArtifacts()
                .stream()
                .map(art -> MavenArtifactSpecification.from(this.getModuleId(), this.getVersionSpec())
                        .customize(c -> {
                            c.setClassifier(art.getClassifier());
                            c.setExtension(art.getExtension());
                            c.setType(art.getType());
                        }))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
