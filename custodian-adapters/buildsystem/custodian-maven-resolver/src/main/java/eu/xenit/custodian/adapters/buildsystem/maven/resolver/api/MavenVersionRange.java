package eu.xenit.custodian.adapters.buildsystem.maven.resolver.api;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;

public class MavenVersionRange {

    public static ResolverVersionSpecification getMajorVersionRange(ResolverArtifactVersion version) {
        return ResolverVersionSpecification.from(String.format("(%s,)", version.toString()));
    }

    public static ResolverVersionSpecification getMinorVersionRange(ResolverArtifactVersion version) {
        return ResolverVersionSpecification.from(
                String.format("(%s,%d.max]", version.toString(), version.getMajorVersion())
        );
    }

    public static ResolverVersionSpecification getIncrementalVersionRange(ResolverArtifactVersion version) {
        return ResolverVersionSpecification.from(
                String.format("(%s,%d.%d.max]",
                        version.toString(),
                        version.getMajorVersion(),
                        version.getMinorVersion())
        );
    }
}
