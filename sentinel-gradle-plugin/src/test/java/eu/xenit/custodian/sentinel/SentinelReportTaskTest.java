package eu.xenit.custodian.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.sentinel.gradle.testkit.GradleBuild;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import org.gradle.internal.impldep.com.esotericsoftware.minlog.Log;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SentinelReportTaskTest {

    @Rule
    public final GradleBuild gradleBuild = new GradleBuild();

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void checkSentinelTask_isNeverUpToDate() throws IOException {
        File directory = temp.newFolder();
        Path source = Paths.get("src/test/resources/examples/simple");
        assertThat(source).exists().isDirectory();
        GradleBuild.copyFolder(source, directory.toPath());

        GradleBuild build = this.gradleBuild.source(directory);

        BuildResult result = build.build("sentinelReport");
        assertThat(result.task(":sentinelReport"))
                .isNotNull()
                .satisfies(sentinel -> assertThat(sentinel.getOutcome()).isEqualByComparingTo(TaskOutcome.SUCCESS));

        // sen sentinel-report task doesn't know what all the inputs are and should disable up to date checks
        BuildResult result2 = build.build("sentinelReport");
        assertThat(result2.task(":sentinelReport"))
                .isNotNull()
                .satisfies(sentinel -> assertThat(sentinel.getOutcome()).isEqualByComparingTo(TaskOutcome.SUCCESS));


    }
}