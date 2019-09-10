package eu.xenit.custodian.adapters.repository.analysis;

import eu.xenit.custodian.domain.model.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.repository.analysis.ProjectAnalyzer;
import java.nio.file.Files;
import java.nio.file.Path;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;

public class SentinelGradlePojectAnalyzer implements ProjectAnalyzer {

    @Override
    public ProjectMetadata analyze(Path location) {
        this.validateLocation(location);

        // locate `sentinel.gradle'
        // Might need to change this into .getResourceAsStream() and write to temp file
        // especially if we're running this from a .jar file
        String sentinelInitScriptPath = getSentinelInitScriptPath();

        GradleRunner gradle = GradleRunner.create()
                .withProjectDir(location.toFile())
//                // debug = true triggers a bug in gradle
//                // see https://github.com/gradle/gradle/issues/3995
//                .withDebug(false)
//                .withEnvironment(Collections.singletonMap("username1", "password1"))
////                .withPluginClasspath()
                .forwardOutput()
                .withArguments("--init-script", sentinelInitScriptPath, "sentinelReport");

        BuildResult buildResult = gradle.build();

        return null;
    }

    private String getSentinelInitScriptPath() {
        return SentinelGradlePojectAnalyzer.class.getClassLoader().getResource("sentinel/sentinel.gradle").getPath();
    }

    private void validateLocation(Path location) {
        if (!Files.exists(location)) {
            throw new IllegalArgumentException("Target location does not exists: "+location);
        }

        if (!Files.isDirectory(location)) {
            throw new IllegalArgumentException("Target location is not a directory: " + location);
        }

        Path buildGradle = location.resolve("build.gradle");
        if (!Files.exists(buildGradle))
        {
            String msg = String.format("Could not find '%s'", buildGradle.toAbsolutePath().normalize());
            throw new IllegalArgumentException(msg);
        }
    }
}
