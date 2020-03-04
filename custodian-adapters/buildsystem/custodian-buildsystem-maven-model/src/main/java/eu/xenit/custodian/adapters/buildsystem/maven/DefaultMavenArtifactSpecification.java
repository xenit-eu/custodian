package eu.xenit.custodian.adapters.buildsystem.maven;

import java.util.Objects;
import lombok.Getter;

@Getter
class DefaultMavenArtifactSpecification implements MavenArtifactSpecification {

    private final MavenModuleIdentifier moduleId;
    private final MavenVersionSpecification versionSpec;
    private final String extension;
    private final String classifier;
    private final String type;

    DefaultMavenArtifactSpecification(MavenModuleIdentifier moduleId, MavenVersionSpecification version,
            String extension, String classifier, String type) {

        Objects.requireNonNull(moduleId, "Argument 'moduleId' is required");
        Objects.requireNonNull(version, "Argument 'version' is required");

        if (type != null && !type.equalsIgnoreCase("jar")) {
            throw new UnsupportedOperationException("artifact type "+type+" is not supported");
        }

        this.moduleId = moduleId;
        this.versionSpec = version;

        this.extension = extension;
        this.classifier = classifier;
        this.type = type;
    }

    /**
     * Using org.eclipse.aether.artifact notation {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}
     *
     * @return the maven artifact coordinates
     */
    @Override
    public String toString() {
        String coords = "maven://"+this.moduleId.getId();

        if (this.extension != null || this.classifier != null) {
            coords += ":";
            if (this.extension != null && !this.extension.equalsIgnoreCase("jar")) {
                coords += this.extension;
            }

            if (this.classifier != null) {
                coords += ":"+this.classifier;
            }
        }

        coords += ":"+this.versionSpec.getValue();
        return coords;
    }
}
