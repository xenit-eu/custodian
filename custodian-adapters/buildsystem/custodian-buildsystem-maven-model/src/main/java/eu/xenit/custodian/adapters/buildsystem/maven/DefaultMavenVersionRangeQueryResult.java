package eu.xenit.custodian.adapters.buildsystem.maven;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultMavenVersionRangeQueryResult implements MavenVersionRangeQueryResult {

    private final Collection<MavenModuleVersion> versions;
    private final MavenVersionSpecification specification;

    public DefaultMavenVersionRangeQueryResult(MavenVersionSpecification specification,
            Collection<MavenModuleVersion> versions) {
        Objects.requireNonNull(versions, "Argument 'versions' is required");
        Objects.requireNonNull(specification, "Argument 'specification' is required");
        this.versions = versions;
        this.specification = specification;
    }

    public DefaultMavenVersionRangeQueryResult(MavenVersionSpecification specification, String... versions) {
        this(specification, Stream.of(versions).map(MavenModuleVersion::from).collect(Collectors.toList()));
    }

    @Override
    public Optional<MavenModuleVersion> getHighestVersion() {
        return this.versions().reduce((first, second) -> second);
    }

    @Override
    public Stream<MavenModuleVersion> versions() {
        return this.versions.stream();
    }

    @Override
    public MavenVersionSpecification getSpecification() {
        return this.specification;
    }
}
