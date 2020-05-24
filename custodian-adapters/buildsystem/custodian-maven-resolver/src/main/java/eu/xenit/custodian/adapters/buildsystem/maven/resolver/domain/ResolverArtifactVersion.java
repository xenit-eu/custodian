package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import java.util.Objects;
import lombok.EqualsAndHashCode;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public interface ResolverArtifactVersion extends Comparable<ResolverArtifactVersion> {

    String getValue();

    int getMajorVersion();
    int getMinorVersion();
    int getIncrementalVersion();
    int getBuildNumber();

    String getQualifier();

    static ResolverArtifactVersion from(String value) {
        Objects.requireNonNull(value, "Argument 'value' is required");
        return new DefaultResolvedVersion(value);
    }
}

@EqualsAndHashCode
class DefaultResolvedVersion implements ResolverArtifactVersion {

    private final org.apache.maven.artifact.versioning.ArtifactVersion internalMavenVersion;

    DefaultResolvedVersion(String version) {
        Objects.requireNonNull(version, "Argument 'version' is required");
        this.internalMavenVersion = new DefaultArtifactVersion(version);
    }

    DefaultResolvedVersion(org.apache.maven.artifact.versioning.ArtifactVersion version) {
        Objects.requireNonNull(version, "Argument 'version' is required");
        this.internalMavenVersion = version;
    }

    @Override
    public String getValue() {
        return this.internalMavenVersion.toString();
    }

    @Override
    public int getMajorVersion() {
        return this.internalMavenVersion.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return this.internalMavenVersion.getMinorVersion();
    }

    @Override
    public int getIncrementalVersion() {
        return this.internalMavenVersion.getIncrementalVersion();
    }

    @Override
    public int getBuildNumber() {
        return this.internalMavenVersion.getBuildNumber();
    }

    @Override
    public String getQualifier() {
        return this.internalMavenVersion.getQualifier();
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public int compareTo(ResolverArtifactVersion version) {
        return this.internalMavenVersion.compareTo(new DefaultArtifactVersion(version.getValue()));
    }
}

