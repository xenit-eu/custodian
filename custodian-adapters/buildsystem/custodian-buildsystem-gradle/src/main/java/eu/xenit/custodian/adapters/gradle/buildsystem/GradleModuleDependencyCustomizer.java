package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleArtifactSpecification.GradleArtifactSpecificationCustomizer;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class GradleModuleDependencyCustomizer {

    private final GradleModuleIdentifier moduleId;
    private final GradleVersionSpecification version;

    private final Set<GradleArtifactSpecification> artifacts = new LinkedHashSet<>();

    public GradleModuleDependencyCustomizer addArtifact(Consumer<GradleArtifactSpecificationCustomizer> callback) {
        GradleArtifactSpecification artifactSpec = GradleArtifactSpecification.from(this.moduleId, this.version);
        this.artifacts.add(artifactSpec.customize(callback));

        return this;
    }

    public Set<GradleArtifactSpecification> getArtifacts() {
        return artifacts;
    }
}
