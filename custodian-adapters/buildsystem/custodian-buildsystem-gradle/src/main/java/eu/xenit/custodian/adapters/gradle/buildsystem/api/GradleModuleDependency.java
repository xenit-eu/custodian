package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactSpecification.GradleArtifactSpecificationCustomizer;
import eu.xenit.custodian.ports.spi.buildsystem.Dependency;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

/**
 * A Gradle {@link Dependency} on a module outside the project hierarchy.
 *
 * This implies a dependency on the default artifact-type of that module. The requested artifact properties (or multiple
 * artifacts) can be further specified.
 *
 * This dependency can be a Maven or Ivy module.
 */
public interface GradleModuleDependency extends GradleDependency {

    GradleModuleIdentifier getModuleId();

    GradleVersionSpecification getVersionSpec();

    Set<GradleArtifactSpecification> getArtifacts();

    default String getGroup() {
        return this.getModuleId().getGroup();
    }

    default String getName() {
        return this.getModuleId().getName();
    }

    default String getId() {
        return this.getGroup() + ":" + this.getName();
    }

    default String getVersion() { return this.getVersionSpec().getValue(); }

    Set<DependencyExcludeRule> getExcludeRules();

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
}
