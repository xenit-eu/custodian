package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.domain.buildsystem.ArtifactSpecification;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;

public interface MavenArtifactSpecification extends ArtifactSpecification {

    MavenModuleIdentifier getModuleId();

    MavenVersionSpecification getVersionSpec();

    String getType();

    String getExtension();

    String getClassifier();

    static MavenArtifactSpecification from(String group, String module, String version) {
        return from(MavenModuleIdentifier.from(group, module), MavenVersionSpecification.from(version));
    }

    static MavenArtifactSpecification from(MavenModuleIdentifier moduleId, MavenVersionSpecification version) {
        return new DefaultMavenArtifactSpecification(moduleId, version, null, null, null);
    }

    default MavenArtifactSpecification customize(Consumer<Customizer> configure) {
        Customizer customizer = new Customizer(
                this.getModuleId(),
                this.getVersionSpec(),
                this.getExtension(),
                this.getClassifier(),
                this.getType()
        );
        configure.accept(customizer);
        return customizer.build();
    }

    @Data
    @AllArgsConstructor
    class Customizer {

        private MavenModuleIdentifier moduleId;
        private MavenVersionSpecification versionSpec;

        private String extension;
        private String classifier;
        private String type;

        private MavenArtifactSpecification build() {
            return new DefaultMavenArtifactSpecification(
                    this.moduleId,
                    this.versionSpec,
                    this.extension,
                    this.classifier,
                    this.type
            );
        }
    }

}
