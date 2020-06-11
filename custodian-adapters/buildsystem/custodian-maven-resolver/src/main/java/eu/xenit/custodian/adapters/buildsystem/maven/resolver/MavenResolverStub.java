package eu.xenit.custodian.adapters.buildsystem.maven.resolver;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether.MavenResolverAdapterStub;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverGroupArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class MavenResolverStub extends MavenResolver {

    public MavenResolverStub(ResolverMavenRepository repo, Stream<ResolverArtifactSpecification> artifacts) {
        this(Map.of(repo, artifacts));
    }

    public MavenResolverStub(Map<ResolverMavenRepository, Stream<ResolverArtifactSpecification>> repoArtifacts) {
        super(new MavenResolverAdapterStub(repoArtifacts));
    }

    @Override
    public VersionRangeQueryResult resolveVersionRange(ResolverGroupArtifact groupArtifact,
            ResolverVersionSpecification version, Set<ResolverArtifact> artifacts,
            Collection<ResolverMavenRepository> repositories) {
        return null;
    }
}
