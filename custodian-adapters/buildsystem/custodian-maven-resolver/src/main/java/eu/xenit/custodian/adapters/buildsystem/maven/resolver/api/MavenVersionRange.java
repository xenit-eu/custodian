package eu.xenit.custodian.adapters.buildsystem.maven.resolver.api;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;

/**
 * The Aether implementation supports more flexible syntax. From the release notes:
 *
 * https://wiki.eclipse.org/Aether/New_and_Noteworthy#Version_Ranges
 */
public class MavenVersionRange {

    // The Aether implementation supports more flexible syntax, but leads to issues with version-bounds-parsing.
    //
    // From the release notes - https://wiki.eclipse.org/Aether/New_and_Noteworthy#Version_Ranges
    // To ease working with version ranges, the version syntax recognized by GenericVersionScheme has been extended
    // to recognize the case-insensitive tokens "min" and "max". These tokens can be used as the last segment of a
    // version string to denote the minimum and maximum value for that segment. For example, using the new syntax,
    // the requirement "depend on version 2.x" translates to the version range "[2.min, 2.max]". Unlike the common
    // attempt to achieve this using "[2.0, 3.0)", the new syntax properly includes "2.0-SNAPSHOT" and excludes
    // "3.0-rc-1". Last but not least, a range of the form "[M.N.min, M.N.max]" can be specified more compact as
    // "[M.N.*]".

    public static ResolverVersionSpecification getMajorVersionRange(ResolverArtifactVersion version) {
        return ResolverVersionSpecification.from(String.format("(%s,)", version.getValue()));
    }

    public static ResolverVersionSpecification getMinorVersionRange(ResolverArtifactVersion version) {
        return ResolverVersionSpecification.from(
                String.format("(%s,%d.999]", version.getValue(), version.getMajorVersion())
        );
    }

    public static ResolverVersionSpecification getIncrementalVersionRange(ResolverArtifactVersion version) {
        return ResolverVersionSpecification.from(
                String.format("(%s,%d.%d.999]",
                        version.getValue(),
                        version.getMajorVersion(),
                        version.getMinorVersion())
        );
    }
}
