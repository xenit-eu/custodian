package eu.xenit.custodian.adapters.buildsystem.maven.resolver.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.DefaultVersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class CompositeMavenVersionRangeQueryResultTest {

    @Test
    public void versions_empty() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                new CompositeMavenVersionRangeQueryResult(Collections.emptyList()));
    }

    @Test
    public void versions_singleQueryResult() {
        ResolverVersionSpecification versionSpec = ResolverVersionSpecification.from("[1.0,)");
        VersionRangeQueryResult queryResult = new CompositeMavenVersionRangeQueryResult(
                new DefaultVersionRangeQueryResult(versionSpec, "1.0", "1.1")
        );

        assertThat(queryResult.versions().map(ResolverArtifactVersion::getValue)).containsExactly("1.0", "1.1");
        assertThat(queryResult.getHighestVersion()).map(ResolverArtifactVersion::getValue).hasValue("1.1");
    }

    @Test
    public void versions_multiQueryResult() {
        ResolverVersionSpecification versionSpec = ResolverVersionSpecification.from("[1.0,)");
        VersionRangeQueryResult queryResult = new CompositeMavenVersionRangeQueryResult(
                new DefaultVersionRangeQueryResult(versionSpec, "1.0", "1.1", "2.0"),
                new DefaultVersionRangeQueryResult(versionSpec, "1.0", "1.1", "1.2", "2.0"),
                new DefaultVersionRangeQueryResult(versionSpec, "1.1", "1.2", "2.0", "2.1")
        );
        assertThat(queryResult.versions().map(ResolverArtifactVersion::getValue))
                .containsExactly("1.1", "2.0");
        assertThat(queryResult.getHighestVersion()).map(ResolverArtifactVersion::getValue).hasValue("2.0");
    }
}