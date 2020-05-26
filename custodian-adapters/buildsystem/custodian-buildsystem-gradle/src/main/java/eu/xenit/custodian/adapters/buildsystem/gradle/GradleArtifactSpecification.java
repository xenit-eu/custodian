package eu.xenit.custodian.adapters.buildsystem.gradle;

import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;

public interface GradleArtifactSpecification {

    GradleModuleIdentifier getModuleId();
    GradleVersionSpecification getVersionSpec();

    /**
     * Returns the name of this artifact.
     *
     * TODO Is this only relevant for Ivy dependencies ?
     */
    String getName();

    String getType();

    String getExtension();

    String getClassifier();

    static GradleArtifactSpecification from(GradleModuleIdentifier moduleId,
            GradleVersionSpecification version) {
        return new DefaultGradleArtifactSpecification(moduleId, version, null, null, null, null);
    }

    static GradleArtifactSpecification from(String group, String name, String version) {
        return from(GradleModuleIdentifier.from(group, name), GradleVersionSpecification.from(version));
    }


    default GradleArtifactSpecification customize(Consumer<GradleArtifactSpecificationCustomizer> configure) {
        GradleArtifactSpecificationCustomizer customizer = new GradleArtifactSpecificationCustomizer(
                this.getModuleId(),
                this.getVersionSpec(),
                this.getName(),
                this.getExtension(),
                this.getClassifier(),
                this.getType()
        );
        configure.accept(customizer);
        return customizer.build();
    }

    @Data
    @AllArgsConstructor
    class GradleArtifactSpecificationCustomizer {

        private final GradleModuleIdentifier moduleId;
        private final GradleVersionSpecification versionSpec;

        private String name;
        private String extension;
        private String classifier;
        private String type;

        private GradleArtifactSpecification build() {
            return new DefaultGradleArtifactSpecification(
                    this.moduleId,
                    this.versionSpec,
                    this.name,
                    this.extension,
                    this.classifier,
                    this.type
            );
        }
    }

}
