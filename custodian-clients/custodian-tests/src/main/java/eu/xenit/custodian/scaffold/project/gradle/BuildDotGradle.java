package eu.xenit.custodian.scaffold.project.gradle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Materialized GradleProject
 */

public class BuildDotGradle {

    private final Path path;

    BuildDotGradle(Path buildDotGradlePath) {
        Objects.requireNonNull(buildDotGradlePath, "Argument 'buildDotGradlePath' is required");

        this.path = buildDotGradlePath;
    }

    public Path getPath() {
        return path;
    }

    public Path getDirectory() {
        return this.path.getParent();
    }

    public void logBuildGradleContent() throws IOException {
        String content = new String(Files.readAllBytes(this.path));
//        log().info(buildDotGradle.toString() + System.lineSeparator() + content);
        System.out.println(path.toAbsolutePath().toString() + System.lineSeparator() + content);

    }
}
