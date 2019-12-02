package eu.xenit.custodian.adapters.buildsystem.maven;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.Test;

public class CompositeMavenVersionRangeQueryResultTest {

    @Test
    public void versions_empty() {
        MavenVersionRangeQueryResult queryResult = new CompositeMavenVersionRangeQueryResult(
                MavenVersionSpecification.from("1.0"), Collections.emptyList());

        assertThat(queryResult.versions()).isEmpty();
        assertThat(queryResult.getHighestVersion()).isNotPresent();
    }

    @Test
    public void versions_singleQueryResult() {
        MavenVersionSpecification versionSpec = MavenVersionSpecification.from("[1.0,)");
        MavenVersionRangeQueryResult queryResult = new CompositeMavenVersionRangeQueryResult(
                versionSpec, new DefaultMavenVersionRangeQueryResult(versionSpec, "1.0", "1.1")
        );

        assertThat(queryResult.versions().map(MavenModuleVersion::getValue)).containsExactly("1.0", "1.1");
        assertThat(queryResult.getHighestVersion()).map(MavenModuleVersion::getValue).hasValue("1.1");
    }

    @Test
    public void versions_multiQueryResult() {
        MavenVersionSpecification versionSpec = MavenVersionSpecification.from("[1.0,)");
        MavenVersionRangeQueryResult queryResult = new CompositeMavenVersionRangeQueryResult(
                versionSpec,
                new DefaultMavenVersionRangeQueryResult(versionSpec, "1.0", "1.1", "2.0"),
                new DefaultMavenVersionRangeQueryResult(versionSpec, "1.0", "1.1", "1.2", "2.0"),
                new DefaultMavenVersionRangeQueryResult(versionSpec, "1.1", "1.2", "2.0", "2.1")
        );
        assertThat(queryResult.versions().map(MavenModuleVersion::getValue))
                .containsExactly("1.1", "2.0");
        assertThat(queryResult.getHighestVersion()).map(MavenModuleVersion::getValue).hasValue("2.0");
    }
}