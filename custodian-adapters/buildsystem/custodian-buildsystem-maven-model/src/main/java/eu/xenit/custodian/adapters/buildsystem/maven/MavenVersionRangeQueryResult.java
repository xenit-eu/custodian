package eu.xenit.custodian.adapters.buildsystem.maven;

import java.util.Optional;
import java.util.stream.Stream;

public interface MavenVersionRangeQueryResult {

    Optional<MavenModuleVersion> getHighestVersion();

    Stream<MavenModuleVersion> versions();

    MavenVersionSpecification getSpecification();
}
