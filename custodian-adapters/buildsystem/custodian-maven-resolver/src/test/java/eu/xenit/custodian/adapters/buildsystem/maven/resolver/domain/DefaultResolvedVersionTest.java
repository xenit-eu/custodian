package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DefaultResolvedVersionTest {

    @Test
    public void testSimple() {
        ResolverArtifactVersion version = new DefaultResolverArtifactVersion("1.2.3.4-SNAPSHOT");

        assertThat(version.getMajorVersion()).isEqualTo(1);
        assertThat(version.getMinorVersion()).isEqualTo(2);
        assertThat(version.getIncrementalVersion()).isEqualTo(3);
        assertThat(version.getQualifier()).isEqualTo("SNAPSHOT");

        assertThat(version.getValue()).isEqualTo("1.2.3.4-SNAPSHOT");
    }


}