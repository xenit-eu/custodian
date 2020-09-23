package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.toolingapi;

import static eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleBuildAssert.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class GradleToolingBuildModelAdapterTest {

    @Test
    void test() throws IOException {
        var path = Paths.get(".");

        var tooling = new GradleToolingBuildReaderAdapter();
        GradleBuild build = tooling.connect(path);

        assertThat(build).isNotNull();

    }

}