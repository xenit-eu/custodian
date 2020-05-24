package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import java.util.Optional;
import java.util.stream.Stream;

public interface VersionRangeQueryResult {

    Optional<ResolverArtifactVersion> getHighestVersion();

    Stream<ResolverArtifactVersion> versions();

    ResolverVersionSpecification getSpecification();
}
