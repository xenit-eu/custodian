package eu.xenit.custodian.adapters.buildsystem.maven;

import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

@EqualsAndHashCode
public class DefaultMavenModuleVersion implements MavenModuleVersion {

    private final ArtifactVersion version;

    DefaultMavenModuleVersion(String version) {
        Objects.requireNonNull(version, "Argument 'version' is required");
        this.version = new DefaultArtifactVersion(version);
    }

    DefaultMavenModuleVersion(ArtifactVersion version) {
        Objects.requireNonNull(version, "Argument 'version' is required");
        this.version = version;
    }

    @Override
    public String getValue() {
        return this.version.toString();
    }

    @Override
    public int getMajorVersion() {
        return this.version.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return this.version.getMinorVersion();
    }

    @Override
    public int getIncrementalVersion() {
        return this.version.getIncrementalVersion();
    }

    @Override
    public int getBuildNumber() {
        return this.version.getBuildNumber();
    }

    @Override
    public String getQualifier() {
        return this.version.getQualifier();
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
