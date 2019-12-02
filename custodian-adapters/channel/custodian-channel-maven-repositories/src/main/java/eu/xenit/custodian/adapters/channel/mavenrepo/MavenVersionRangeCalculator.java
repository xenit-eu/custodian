package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;

class MavenVersionRangeCalculator {

    public static MavenVersionSpecification getMajorUpdateVersionRange(MavenModuleVersion version) {
        return MavenVersionSpecification.from(String.format("[%s,)", version.toString()));
    }

    public static MavenVersionSpecification getMinorUpdateVersionRange(MavenModuleVersion version) {
        return MavenVersionSpecification.from(
                // FIXME - does the upper bound includes SNAPSHOT versions of the next major release ??
                String.format("[%s,%d)", version.toString(), version.getMajorVersion() + 1)
        );
    }

    public static MavenVersionSpecification getIncrementalVersionRange(MavenModuleVersion version) {
        return MavenVersionSpecification.from(
                String.format("[%s,%d.%d)",
                        version.toString(),
                        version.getMajorVersion(),
                        version.getMinorVersion() + 1)
        );
    }
}
