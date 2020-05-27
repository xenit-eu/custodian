package eu.xenit.custodian.adapters.gradle.buildsystem;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.ports.spi.buildsystem.BuildSystem;
import org.junit.jupiter.api.Test;

public class GradleBuildSystemFactoryTest {

    @Test
    public void buildSystemForId() {
        BuildSystem gradle = BuildSystem.forId(GradleBuildSystem.ID);

        assertThat(gradle).isNotNull();
        assertThat(gradle.id()).isEqualTo("gradle");
    }
}