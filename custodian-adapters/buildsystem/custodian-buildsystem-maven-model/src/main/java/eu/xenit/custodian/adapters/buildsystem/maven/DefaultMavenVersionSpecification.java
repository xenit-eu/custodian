package eu.xenit.custodian.adapters.buildsystem.maven;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.Restriction;
import org.apache.maven.artifact.versioning.VersionRange;

public class DefaultMavenVersionSpecification implements MavenVersionSpecification {

    private final String value;
    private final VersionRange versionRange;

    DefaultMavenVersionSpecification(String value) {
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
    public Optional<MavenModuleVersion> asPinnedVersion() {
        // The spec could not be properly parsed
        if (this.versionRange == null) {
            return Optional.empty();
        }

        // The spec has ranges, no pinned version
        ArtifactVersion recommendedVersion = this.versionRange.getRecommendedVersion();
        if (recommendedVersion == null) {
            return Optional.empty();
        }

        // Spec contains an explicit version
        return Optional.of(new DefaultMavenModuleVersion(recommendedVersion));
    }

    @Data
    @RequiredArgsConstructor
    static class DefaultVersionBounds implements VersionBounds {

        private final MavenModuleVersion lowerBound;
        private final boolean lowerBoundInclusive;
        private final MavenModuleVersion upperBound;
        private final boolean upperBoundInclusive;

        DefaultVersionBounds(Restriction r) {
            this.lowerBound = r.getLowerBound() == null ? null : new DefaultMavenModuleVersion(r.getLowerBound());
            this.lowerBoundInclusive = r.isLowerBoundInclusive();
            this.upperBound = r.getUpperBound() == null ? null : new DefaultMavenModuleVersion(r.getUpperBound());
            this.upperBoundInclusive = r.isUpperBoundInclusive();
        }

    }
}
