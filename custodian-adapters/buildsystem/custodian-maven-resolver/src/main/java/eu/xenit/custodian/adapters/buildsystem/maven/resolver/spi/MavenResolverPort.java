package eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import java.util.Collection;

public interface MavenResolverPort {

    VersionRangeQueryResult resolveVersionRange(
            Collection<ResolverMavenRepository> repositories,
            ResolverArtifactSpecification spec);

}
