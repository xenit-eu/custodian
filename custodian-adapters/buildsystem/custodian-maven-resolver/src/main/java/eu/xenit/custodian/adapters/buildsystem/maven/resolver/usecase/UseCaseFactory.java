package eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.MavenResolverPort;

public class UseCaseFactory {

    private final MavenResolverPort resolverPort;

    public UseCaseFactory(MavenResolverPort resolverPort) {
        this.resolverPort = resolverPort;
    }

    public ResolveArtifactsVersionRangeUseCase createResolveArtifactsVersionRangeUseCase() {
        return new DefaultResolveArtifactsVersionRangeUseCase(this.resolverPort);
    }
}
