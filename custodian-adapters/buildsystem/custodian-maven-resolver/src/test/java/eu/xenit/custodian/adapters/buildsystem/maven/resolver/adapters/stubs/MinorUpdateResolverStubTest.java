package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.stubs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class MinorUpdateResolverStubTest {

    @Test
    void resolveVersionRange() {
        var resolver = new MinorUpdateResolverStub();

        var versionSpec = "[2.3.4,2.999]";
        var versionQueryResult = resolver.resolveVersionRange(
                Collections.emptyList(),
                ResolverArtifactSpecification.from("eu.xenit", "custodian", versionSpec));

        assertThat(versionQueryResult).isNotNull();
        assertThat(versionQueryResult.getHighestVersion())
                .isPresent()
                .hasValueSatisfying(version -> {
                    // the version can be 2.4 or 2.4.0 ?
                    // in maven semantics, that is equivalent
                    assertThat(version.compareTo(ResolverArtifactVersion.from(2,4))).isEqualTo(0);
                });

    }
}