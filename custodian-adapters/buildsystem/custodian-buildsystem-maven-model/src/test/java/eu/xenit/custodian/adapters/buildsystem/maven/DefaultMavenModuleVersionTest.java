package eu.xenit.custodian.adapters.buildsystem.maven;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DefaultMavenModuleVersionTest {

    @Test
    public void testEquals() {
        assertThat(new DefaultMavenModuleVersion("1.2.3"))
                .isEqualTo(new DefaultMavenModuleVersion("1.2.3"));
    }
}