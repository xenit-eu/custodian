package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultVersionRangeQueryResult implements VersionRangeQueryResult {

    private final Collection<ResolverArtifactVersion> versions;
    private final ResolverVersionSpecification specification;

    public DefaultVersionRangeQueryResult(ResolverVersionSpecification specification,
            Collection<ResolverArtifactVersion> versions) {
        Objects.requireNonNull(versions, "Argument 'versions' is required");
        Objects.requireNonNull(specification, "Argument 'specification' is required");
        this.versions = versions;
        this.specification = specification;
    }

    public DefaultVersionRangeQueryResult(ResolverVersionSpecification specification, String... versions) {
        this(specification, Stream.of(versions).map(ResolverArtifactVersion::from).collect(Collectors.toList()));
    }

    public DefaultVersionRangeQueryResult(String versionSpec, String... versions) {
        this(ResolverVersionSpecification.from(versionSpec), versions);
    }

    @Override
    public Optional<ResolverArtifactVersion> getHighestVersion() {
        return this.versions().reduce((first, second) -> second);
    }

    @Override
    public Stream<ResolverArtifactVersion> versions() {
        return this.versions.stream();
    }

    @Override
    public ResolverVersionSpecification getSpecification() {
        return this.specification;
    }
}
