package eu.xenit.custodian.adapters.buildsystem.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuildSystem;
import eu.xenit.custodian.ports.spi.build.BuildSystem;
import org.junit.Test;

public class GradleBuildSystemFactoryTest {

    @Test
    public void buildSystemForId() {
        BuildSystem gradle = BuildSystem.forId(GradleBuildSystem.ID);

        assertThat(gradle).isNotNull();
        assertThat(gradle.id()).isEqualTo("gradle");
    }
}