package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationProvider;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleArtifactSpecification.GradleArtifactSpecificationCustomizer;
import eu.xenit.custodian.domain.buildsystem.Dependency;
import eu.xenit.custodian.domain.buildsystem.ExternalModuleDependency;
import eu.xenit.custodian.domain.buildsystem.GroupArtifactModuleIdentifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

/**
 * A Gradle {@link Dependency} on a module outside the project hierarchy.
 *
 * This implies a dependency on the default artifact-type of that module.
 * The requested artifact properties (or multiple artifacts) can be further specified.
 *
 * This dependency can be a Maven or Ivy module.
 */
public interface GradleModuleDependency extends GradleDependency,
        ExternalModuleDependency,
        MavenArtifactSpecificationProvider
        /*, ArtifactSpecificationDescriptor<IvyArtifactSpecification> */ {

    GroupArtifactModuleIdentifier getModuleId();
    GradleVersionSpecification getVersionSpec();

    Set<GradleArtifactSpecification> getArtifacts();

    default String getGroup() {
        return this.getModuleId().getGroup();
    }

    default String getName() {
        return this.getModuleId().getName();
    }


    static GradleModuleDependency from(String configuration, GroupArtifactModuleIdentifier module, GradleVersionSpecification version)
    {
        return from(configuration, module, version, dependency -> {
            // should we set default artifact specs here ?
            // or is that not our responsibility ?
        });
    }
    static GradleModuleDependency from(String configuration, GroupArtifactModuleIdentifier module, GradleVersionSpecification version,
            Consumer<GradleModuleDependencyCustomizer> callback) {
        GradleModuleDependencyCustomizer customizer = new GradleModuleDependencyCustomizer(module, version);
        callback.accept(customizer);
        return new DefaultGradleModuleDependency(configuration, module, version, customizer.getArtifacts());
    }


    @RequiredArgsConstructor
    class GradleModuleDependencyCustomizer {

        private final GroupArtifactModuleIdentifier module;
        private final GradleVersionSpecification version;

        private final Set<GradleArtifactSpecification> artifacts = new LinkedHashSet<>();

        public GradleModuleDependencyCustomizer addArtifact(Consumer<GradleArtifactSpecificationCustomizer> callback) {
            GradleArtifactSpecification artifactSpec = GradleArtifactSpecification.from(this.module, this.version);
            this.artifacts.add(artifactSpec.customize(callback));

            return this;
        }

        public Set<GradleArtifactSpecification> getArtifacts() {
            return artifacts;
        }
    }


}
