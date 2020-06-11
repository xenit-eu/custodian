package eu.xenit.custodian.adapters.buildsystem.maven.resolver.api;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.GroupArtifactVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverGroupArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import java.util.Collection;
import java.util.Set;

public interface MavenResolverApi {

    VersionRangeQueryResult resolveVersionRange(
            ResolverGroupArtifact groupArtifact,
            ResolverVersionSpecification version,
            Set<ResolverArtifact> artifacts,
            Collection<ResolverMavenRepository> repositories);


    default VersionRangeQueryResult resolveVersionRange(GroupArtifactVersionSpecification gav,
            ResolverArtifact artifact,
            Collection<ResolverMavenRepository> repositories) {
        return this.resolveVersionRange(gav.getGroupArtifact(), gav.getVersion(), Set.of(artifact), repositories);
    }
}
