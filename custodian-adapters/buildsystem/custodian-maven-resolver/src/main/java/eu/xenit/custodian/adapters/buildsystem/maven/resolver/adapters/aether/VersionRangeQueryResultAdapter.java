package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

public class VersionRangeQueryResultAdapter implements VersionRangeQueryResult {

    private final VersionRangeResult versionRangeResult;

    VersionRangeQueryResultAdapter(VersionRangeResult versionRangeResult) {

        this.versionRangeResult = versionRangeResult;
    }

    public Optional<ResolverArtifactVersion> getHighestVersion() {
        return Optional.ofNullable(this.versionRangeResult.getHighestVersion())
                .map(Version::toString)
                .map(ResolverArtifactVersion::from);
    }

    public Stream<ResolverArtifactVersion> versions() {
        return this.versionRangeResult.getVersions()
                .stream()
                .map(Version::toString)
                .map(ResolverArtifactVersion::from);
    }

    @Override
    public ResolverVersionSpecification getSpecification() {
        return ResolverVersionSpecification.from(this.versionRangeResult.getRequest().getArtifact().getVersion());
    }

    @Override
    public String toString() {
        String highest = this.getHighestVersion().map(ResolverArtifactVersion::getValue).orElse("");
        return "[" + this.versions()
                .map(ResolverArtifactVersion::getValue)
                .map(version -> {
                    if (highest.equalsIgnoreCase(version)) {
                        version += "*";
                    }
                    return version;
                })
                .collect(Collectors.joining(",")) + "]";
    }
}
