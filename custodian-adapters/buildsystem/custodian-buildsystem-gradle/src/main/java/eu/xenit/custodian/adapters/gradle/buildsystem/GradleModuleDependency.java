package eu.xenit.custodian.adapters.gradle.buildsystem;

import eu.xenit.custodian.ports.spi.buildsystem.Dependency;
import java.util.Set;
import java.util.function.Consumer;

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

    static GradleModuleDependency from(String configuration, String group, String module, String version) {
        return from(
                configuration,
                GradleModuleIdentifier.from(group, module),
                GradleVersionSpecification.from(version));
    }

    static GradleModuleDependency from(String configuration, GradleModuleIdentifier module,
            GradleVersionSpecification version) {
        return from(configuration, module, version, dependency -> {
            // should we set default artifact specs here ?
            // or is that not our responsibility ?
        });
    }

    static GradleModuleDependency from(String configuration, GradleModuleIdentifier module,
            GradleVersionSpecification version,
            Consumer<GradleModuleDependencyCustomizer> callback) {
        GradleModuleDependencyCustomizer customizer = new GradleModuleDependencyCustomizer(module, version);
        callback.accept(customizer);
        return new DefaultGradleModuleDependency(configuration, module, version, customizer.getArtifacts());
    }

    static GradleModuleDependency implementation(GradleModuleIdentifier module, String version) {
        return from("implementation", module, GradleVersionSpecification.from(version));
    }

}
