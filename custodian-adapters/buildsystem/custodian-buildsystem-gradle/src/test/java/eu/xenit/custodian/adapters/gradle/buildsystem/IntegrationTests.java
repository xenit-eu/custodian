package eu.xenit.custodian.adapters.gradle.buildsystem;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleBuildAssert;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegrationTests {

    @Test
    public void inception() throws BuildSystemException {
        GradleBuildSystemAdapter gradle = new GradleBuildSystemAdapter();
        Path location = Paths.get("../../..");

        Optional<GradleBuild> build = gradle.getBuild(location);

        Assertions.assertThat(build).isPresent();

        GradleBuildAssert.assertThat(build.get())
                .isNotNull()
                .assertRootProject().hasName("custodian");

    }
}
