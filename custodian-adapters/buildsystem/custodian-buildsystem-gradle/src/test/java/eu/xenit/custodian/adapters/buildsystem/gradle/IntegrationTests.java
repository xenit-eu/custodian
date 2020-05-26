package eu.xenit.custodian.adapters.buildsystem.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.domain.buildsystem.GradleBuildAssert;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class IntegrationTests {

    @Test
    public void inception() throws BuildSystemException {
        GradleBuildSystemAdapter sentinel = new GradleBuildSystemAdapter();
        Path location = Paths.get("../../..");

        Optional<GradleBuild> build = sentinel.getBuild(location);

        assertThat(build).isPresent();

        GradleBuildAssert.assertThat(build.get())
                .isNotNull()
                .assertRootProject().hasName("custodian");

    }
}
