package eu.xenit.custodian.adapters.repository.analysis;

import eu.xenit.custodian.adapters.model.build.gradle.GradleBuild;
import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.repository.analysis.AnalyzerException;
import eu.xenit.custodian.domain.repository.analysis.ProjectAnalyzer;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentinelGradlePojectAnalyzer implements ProjectAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(SentinelGradlePojectAnalyzer.class);

    private static final String SENTINEL_REPORT_TASK = "sentinelReport";

    @Override
    public ProjectMetadata analyze(ProjectHandle handle) throws AnalyzerException {
        Path location = handle.getLocation();
        this.validateLocation(location);

        try {
            this.runSentinelReport(location);
        } catch (IOException e) {
            throw new AnalyzerException(e);
        }

        // check the build succeeded ?

        return new ProjectMetadata(handle.getReference());
    }

    GradleBuild runSentinelReport(Path location) throws IOException {
        // locate `sentinel.gradle'
        // Might need to change this into .getResourceAsStream() and write to temp file
        // especially if we're running this from a .jar file
        URL sentinelInit = SentinelGradlePojectAnalyzer.class.getClassLoader().getResource("sentinel/sentinel.gradle");
        if (sentinelInit == null) {
            throw new IOException("Could not locate sentinel initscript");
        }
        String sentinelInitScriptPath = sentinelInit.getPath();

        GradleRunner runner = GradleRunner.create()
                .withProjectDir(location.toFile())
//                // debug = true triggers a bug in gradle
//                // see https://github.com/gradle/gradle/issues/3995
//                .withDebug(false)
//                .withEnvironment(Collections.singletonMap("username1", "password1"))
//                .withPluginClasspath()
                .forwardOutput()
                .withArguments("--init-script", sentinelInitScriptPath, SENTINEL_REPORT_TASK);

        BuildResult buildResult = runner.build();

        buildResult.getTasks().forEach(task -> {
            log.info("task: {} - {}", task.getPath(), task.getOutcome());
        });


        return null;
//        return new GradleBuild();

    }

    private void validateLocation(Path location) {
        if (!Files.exists(location)) {
            throw new IllegalArgumentException("Target location does not exists: " + location);
        }

        if (!Files.isDirectory(location)) {
            throw new IllegalArgumentException("Target location is not a directory: " + location);
        }

        Path buildGradle = location.resolve("build.gradle");
        if (!Files.exists(buildGradle)) {
            String msg = String.format("Could not find '%s'", buildGradle.toAbsolutePath().normalize());
            throw new IllegalArgumentException(msg);
        }
    }
}
