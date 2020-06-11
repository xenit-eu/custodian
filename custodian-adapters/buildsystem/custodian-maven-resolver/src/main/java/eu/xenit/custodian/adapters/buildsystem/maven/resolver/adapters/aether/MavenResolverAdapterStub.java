package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.MavenRepositoryStub;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.MavenResolverPort;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public class MavenResolverAdapterStub implements MavenResolverPort {

    private final MavenResolverPort delegate;

    public MavenResolverAdapterStub(Stream<MavenRepositoryStub> repositories) {
        this.delegate = new MavenResolverAdapter(new RepositorySystemStub(repositories));
    }

    public MavenResolverAdapterStub(Map<ResolverMavenRepository, Stream<ResolverArtifactSpecification>> repoArtifacts) {
        this(repoArtifacts.entrySet().stream().map(entry -> new MavenRepositoryStub(entry.getKey(), entry.getValue())));
    }

    @Override
    public VersionRangeQueryResult resolveVersionRange(Collection<ResolverMavenRepository> repositories,
            ResolverArtifactSpecification spec) {

        return this.delegate.resolveVersionRange(repositories, spec);
    }
}
