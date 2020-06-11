package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ResolverGroupArtifact {

    String getGroupId();

    String getArtifactId();

    /**
     * Creates a new instance
     *
     * @param groupId The group identifier of the module, may be {@code null}.
     * @param artifactId The artifact identifier of the module, may be {@code null}.
     * @return the maven coordinates
     */
    static ResolverGroupArtifact from(String groupId, String artifactId) {
        return new DefaultGroupArtifactSpecification(groupId, artifactId);
    }

    @Getter
    @AllArgsConstructor
    class DefaultGroupArtifactSpecification implements ResolverGroupArtifact {
        String groupId;
        String artifactId;
    }

}
