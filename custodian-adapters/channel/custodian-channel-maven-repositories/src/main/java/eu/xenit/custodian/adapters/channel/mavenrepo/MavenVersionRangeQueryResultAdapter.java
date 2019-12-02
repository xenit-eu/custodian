package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import eu.xenit.custodian.domain.buildsystem.ModuleVersion;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

public class MavenVersionRangeQueryResultAdapter implements MavenVersionRangeQueryResult {

    private final VersionRangeResult versionRangeResult;

    MavenVersionRangeQueryResultAdapter(VersionRangeResult versionRangeResult) {

        this.versionRangeResult = versionRangeResult;
    }

    public Optional<MavenModuleVersion> getHighestVersion() {
        return Optional.ofNullable(this.versionRangeResult.getHighestVersion())
                .map(Version::toString)
                .map(MavenModuleVersion::from);
    }

    public Stream<MavenModuleVersion> versions() {
        return this.versionRangeResult.getVersions()
                .stream()
                .map(Version::toString)
                .map(MavenModuleVersion::from);
    }

    @Override
    public MavenVersionSpecification getSpecification() {
        return MavenVersionSpecification.from(this.versionRangeResult.getRequest().getArtifact().getVersion());
    }

    @Override
    public String toString() {
        String highest = this.getHighestVersion().map(MavenModuleVersion::getValue).orElse("");
        return "[" + this.versions()
                .map(MavenModuleVersion::getValue)
                .map(version -> {
                    if (highest.equalsIgnoreCase(version)) {
                        version += "*";
                    }
                    return version;
                })
                .collect(Collectors.joining(",")) + "]";
    }
}
