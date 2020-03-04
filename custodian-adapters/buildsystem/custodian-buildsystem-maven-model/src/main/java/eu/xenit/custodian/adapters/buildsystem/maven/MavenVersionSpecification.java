package eu.xenit.custodian.adapters.buildsystem.maven;

import eu.xenit.custodian.adapters.buildsystem.maven.DefaultMavenVersionSpecification.DefaultVersionBounds;
import eu.xenit.custodian.domain.buildsystem.VersionSpecification;
import java.util.List;
import java.util.Optional;

public interface MavenVersionSpecification extends VersionSpecification {

    boolean isValid();

    List<VersionBounds> getBounds();

    boolean isPinnedVersion();
    Optional<MavenModuleVersion> asPinnedVersion();

    static MavenVersionSpecification from(String version) {
        return new DefaultMavenVersionSpecification(version);
    }

    static MavenVersionSpecification from(VersionSpecification version) {
        return new DefaultMavenVersionSpecification(version.getValue());
    }

    static MavenVersionSpecification from(MavenModuleVersion version) {
        return new DefaultMavenVersionSpecification(version.getValue());
    }


    interface VersionBounds {

        MavenModuleVersion getLowerBound();
        boolean isLowerBoundInclusive();

        MavenModuleVersion getUpperBound();
        boolean isUpperBoundInclusive();

        static VersionBounds from(String lowerBound, boolean lowerBoundInclusive, String upperBound, boolean upperBoundInclusive)
        {
            return new DefaultVersionBounds(
                    lowerBound == null ? null : MavenModuleVersion.from(lowerBound),
                    lowerBoundInclusive,
                    upperBound == null ? null : MavenModuleVersion.from(upperBound),
                    upperBoundInclusive
            );
        }
        static VersionBounds fromLowerBounds(String lowerBound, boolean lowerBoundInclusive) {
            return from(lowerBound, lowerBoundInclusive, null, false);
        }

        static VersionBounds fromUpperBounds(String upperBound, boolean upperBoundInclusive) {
            return from(null, false, upperBound, upperBoundInclusive);
        }
    }
}
