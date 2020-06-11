package eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverGroupArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.MavenResolverPort;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Value;

public interface ResolveArtifactsVersionRangeUseCase {

    VersionRangeQueryResult handle(ResolveArtifactsCommand command);

    @Value
    class ResolveArtifactsCommand {
        ResolverGroupArtifact groupArtifact;
        ResolverVersionSpecification versionSpec;
        Set<ResolverArtifact> artifacts;
        Collection<ResolverMavenRepository> repositories;
    }
}

class DefaultResolveArtifactsVersionRangeUseCase implements ResolveArtifactsVersionRangeUseCase {

    private final MavenResolverPort resolverPort;

    public DefaultResolveArtifactsVersionRangeUseCase(MavenResolverPort resolverPort) {

        this.resolverPort = resolverPort;
    }

    @Override
    public VersionRangeQueryResult handle(ResolveArtifactsCommand command) {

        // we have to resolve multiple artifacts for the same GAV
        // but the resolver-port can only resolve one artifact at a time
        // so we have to loop over the artifacts and return the intersection

        // if the provided argument is empty, fall back to default classifier/extension
        Set<ResolverArtifact> artifacts = command.getArtifacts();
        if (artifacts.isEmpty()) {
            artifacts = Set.of(ResolverArtifact.defaultArtifact());
        }

        // lookup for every artifact
        List<VersionRangeQueryResult> collect = artifacts.stream()
                .map(art -> ResolverArtifactSpecification.from(command.getGroupArtifact(), command.getVersionSpec(), art))
                .map(spec -> this.resolverPort.resolveVersionRange(command.getRepositories(), spec))
                .collect(Collectors.toList());

        // if there is only one artifact, that's the result
        if (collect.size() == 1) {
            return collect.get(0);
        }

        // if there are multiple artifacts, they need to be combined (intersection of available versions)
        return new CompositeMavenVersionRangeQueryResult(collect);
    }
}