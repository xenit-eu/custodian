package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GradleBuildFileAssertions {

    public static GradleBuildFileAssert assertThat(Path buildGradlePath) throws IOException {
        return assertThat(Files.readString(buildGradlePath));
    }

    public static GradleBuildFileAssert assertThat(String content) {
        return new GradleBuildFileAssert(content);
    }
}
