package eu.xenit.custodian.adapters.buildsystem.maven.resolver.api;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import java.util.Optional;
import java.util.stream.Stream;

public interface VersionRangeQueryResult {

    Optional<ResolverArtifactVersion> getHighestVersion();

    Stream<ResolverArtifactVersion> versions();

    ResolverVersionSpecification getSpecification();
}
