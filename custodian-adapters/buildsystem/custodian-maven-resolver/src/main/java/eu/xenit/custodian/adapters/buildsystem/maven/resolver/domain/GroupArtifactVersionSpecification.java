package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Maven module coordinates.
 *
 * This is an immutable/value class
 */
public interface GroupArtifactVersionSpecification {

    String getGroupId();

    String getArtifactId();

    ResolverVersionSpecification getVersion();

    default ResolverGroupArtifact getGroupArtifact() {
        return ResolverGroupArtifact.from(this.getGroupId(), this.getArtifactId());
    }

    /**
     * Creates new maven coordinates.
     *
     * @param groupId The group identifier of the module, may be {@code null}.
     * @param artifactId The artifact identifier of the module, may be {@code null}.
     * @param version The version of the module, may be {@code null}.
     * @return the maven coordinates
     */
    static GroupArtifactVersionSpecification from(String groupId, String artifactId, String version) {
        return new DefaultGroupArtifactVersionSpecification(
                groupId, artifactId, ResolverVersionSpecification.from(version));
    }

    @Getter
    @AllArgsConstructor
    class DefaultGroupArtifactVersionSpecification implements GroupArtifactVersionSpecification {

        String groupId;
        String artifactId;
        ResolverVersionSpecification version;
    }
}
