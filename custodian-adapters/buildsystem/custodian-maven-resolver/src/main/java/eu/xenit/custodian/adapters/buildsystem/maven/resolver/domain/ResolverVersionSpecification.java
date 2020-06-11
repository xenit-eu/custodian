package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.DefaultResolverVersionSpecification.DefaultVersionBounds;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.Restriction;
import org.apache.maven.artifact.versioning.VersionRange;

public interface ResolverVersionSpecification {

    String getValue();

    boolean isValid();

    List<VersionBounds> getBounds();

    boolean isPinnedVersion();
    Optional<ResolverArtifactVersion> asPinnedVersion();

    static ResolverVersionSpecification from(String version) {
        return new DefaultResolverVersionSpecification(version);
    }

    static ResolverVersionSpecification from(ResolverArtifactVersion version) {
        return new DefaultResolverVersionSpecification(version.getValue());
    }

    default boolean matches(String version) {
        return this.matches(ResolverArtifactVersion.from(version));
    }

    default boolean matches(ResolverArtifactVersion version) {
        if (this.isPinnedVersion()) {
            // exact version match check
            return this.getValue().equalsIgnoreCase(version.getValue());
        } else {
            for (var bound : this.getBounds()) {
                if (bound.containsVersion(version)) {
                    return true;
                }
            }
            return false;
        }
    }


    interface VersionBounds {

        ResolverArtifactVersion getLowerBound();
        boolean isLowerBoundInclusive();

        ResolverArtifactVersion getUpperBound();
        boolean isUpperBoundInclusive();

        static VersionBounds from(String lowerBound, boolean lowerBoundInclusive, String upperBound,
                boolean upperBoundInclusive)
        {
            return new DefaultVersionBounds(
                    lowerBound == null ? null : ResolverArtifactVersion.from(lowerBound),
                    lowerBoundInclusive,
                    upperBound == null ? null : ResolverArtifactVersion.from(upperBound),
                    upperBoundInclusive
            );
        }
        static VersionBounds fromLowerBounds(String lowerBound, boolean lowerBoundInclusive) {
            return from(lowerBound, lowerBoundInclusive, null, false);
        }

        static VersionBounds fromUpperBounds(String upperBound, boolean upperBoundInclusive) {
            return from(null, false, upperBound, upperBoundInclusive);
        }

        default boolean containsVersion(ResolverArtifactVersion version) {
            if (this.getLowerBound() != null) {
                int compare = this.getLowerBound().compareTo(version);

                if (!this.isLowerBoundInclusive() && compare == 0) {
                    // matches the lower bound exactly, but the bound is exclusive
                    return false;
                }

                // if the value is strictly positive, the lower bound is higher than the version
                if (compare > 0) {
                    return false;
                }
            }

            if (this.getUpperBound() != null) {
                int compare = this.getUpperBound().compareTo(version);

                if (!this.isUpperBoundInclusive() && compare == 0) {
                    // matches the upper bound exactly, but the bound is exclusive
                    return false;
                }

                if (compare < 0) {
                    return false;
                }
            }

            return true;
        }
    }
}

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class DefaultResolverVersionSpecification implements ResolverVersionSpecification {

    @EqualsAndHashCode.Include
    private final String value;

    private final VersionRange versionRange;

    DefaultResolverVersionSpecification(String value) {
        this.value = value == null ? "" : value;
        this.versionRange = tryParseVersionRange(value);
    }

    private static VersionRange tryParseVersionRange(String value) {
        try {
            return VersionRange.createFromVersionSpec(value);
        } catch (InvalidVersionSpecificationException e) {
            return null;
        }
    }

    @Override
    public List<VersionBounds> getBounds() {
        if (!this.isValid()) {
            return Collections.emptyList();
        }

        return this.versionRange.getRestrictions().stream()
                .map(DefaultVersionBounds::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean isValid() {
        return this.versionRange != null;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public boolean isPinnedVersion() {
        if (versionRange == null) {
            return false;
        }

        // Check the VersionRange parser found a recommended version
        if (this.versionRange.getRecommendedVersion() == null) return false;

        // Check there is a single unbounded restriction
        if (this.versionRange.getRestrictions().size() != 1) return false;
        Restriction r = this.versionRange.getRestrictions().get(0);
        return r.getLowerBound() == null && r.getUpperBound() == null;
    }

    @Override
    public Optional<ResolverArtifactVersion> asPinnedVersion() {
        // The spec could not be properly parsed
        if (this.versionRange == null) {
            return Optional.empty();
        }

        // The spec has ranges, no pinned version
        org.apache.maven.artifact.versioning.ArtifactVersion recommendedVersion = this.versionRange.getRecommendedVersion();
        if (recommendedVersion == null) {
            return Optional.empty();
        }

        // Spec contains an explicit version
        return Optional.of(new DefaultResolverArtifactVersion(recommendedVersion.toString()));
    }

    @Data
    @RequiredArgsConstructor
    static class DefaultVersionBounds implements VersionBounds {

        private final ResolverArtifactVersion lowerBound;
        private final boolean lowerBoundInclusive;
        private final ResolverArtifactVersion upperBound;
        private final boolean upperBoundInclusive;

        DefaultVersionBounds(Restriction r) {
            this.lowerBound = r.getLowerBound() == null ? null : new DefaultResolverArtifactVersion(r.getLowerBound().toString());
            this.lowerBoundInclusive = r.isLowerBoundInclusive();
            this.upperBound = r.getUpperBound() == null ? null : new DefaultResolverArtifactVersion(r.getUpperBound().toString());
            this.upperBoundInclusive = r.isUpperBoundInclusive();
        }

    }
}