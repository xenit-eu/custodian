package eu.xenit.custodian.adapters.buildsystem.maven.resolver;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether.MavenResolverAdapter;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenResolverApi;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.GroupArtifactVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverGroupArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.VersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.MavenResolverPort;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase.ResolveArtifactsVersionRangeUseCase;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase.ResolveArtifactsVersionRangeUseCase.ResolveArtifactsCommand;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase.UseCaseFactory;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class MavenResolver implements MavenResolverApi {

    private final ResolveArtifactsVersionRangeUseCase resolveArtifactsUseCase;

    public MavenResolver(MavenResolverPort resolverPort) {

        UseCaseFactory useCaseFactory = new UseCaseFactory(resolverPort);
        this.resolveArtifactsUseCase = useCaseFactory.createResolveArtifactsVersionRangeUseCase();
    }

    public MavenResolver() {
        this(new MavenResolverAdapter());
    }

    @Override
    public VersionRangeQueryResult resolveVersionRange(ResolverGroupArtifact groupArtifact,
            ResolverVersionSpecification versionSpec,
            Set<ResolverArtifact> artifacts, Collection<ResolverMavenRepository> repositories) {
        Objects.requireNonNull(groupArtifact, "Argument 'groupArtifact' is required");
        Objects.requireNonNull(versionSpec, "Argument 'versionSpec' is required");
        Objects.requireNonNull(artifacts, "Argument 'artifacts' is required");
        Objects.requireNonNull(repositories, "Argument 'repositories' is required");

        ResolveArtifactsCommand command = new ResolveArtifactsCommand(groupArtifact, versionSpec, artifacts,
                repositories);

        return this.resolveArtifactsUseCase.handle(command);
    }
}
