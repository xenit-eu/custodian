package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import java.util.Objects;
import java.util.StringTokenizer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

@Getter
@EqualsAndHashCode
class DefaultResolverArtifactVersion implements ResolverArtifactVersion {

    private String value;

    private Integer major;
    private Integer minor;
    private Integer incremental;

    private String qualifier;


    DefaultResolverArtifactVersion(String version) {
        Objects.requireNonNull(version, "Argument 'version' is required");

        this.value = version;
        this.parse(version);
    }

    private void parse(String version) {
        int index = version.indexOf('-');

        String part1;

        if ( index < 0 )
        {
            part1 = version;
        }
        else
        {
            part1 = version.substring( 0, index );
            this.qualifier = version.substring( index + 1 );
        }

        StringTokenizer tokenizer = new StringTokenizer(part1, ".");

        this.major = Integer.parseInt(tokenizer.nextToken());
        if (tokenizer.hasMoreTokens()) {
            this.minor = Integer.parseInt(tokenizer.nextToken());
        }

        if (tokenizer.hasMoreTokens()) {
            this.incremental = Integer.parseInt(tokenizer.nextToken());
        }

        // ignoring more tokens (like 4-part version number)
    }

    @Override
    public int getMajorVersion() {
        return this.major;
    }

    @Override
    public int getMinorVersion() {
        return this.minor;
    }

    @Override
    public int getIncrementalVersion() {
        return this.incremental;
    }

    @Override
    public int compareTo(ResolverArtifactVersion other) {
        return new DefaultArtifactVersion(this.getValue()).compareTo(new DefaultArtifactVersion(other.getValue()));
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
