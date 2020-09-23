package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto.Project;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto.Report;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.gradle.testkit.runner.internal.DefaultBuildResult;
import org.gradle.testkit.runner.internal.DefaultBuildTask;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.defaultanswers.TriesToReturnSelf;

public class SentinelBuildModelAdapterTest {

    @Test
    public void testInvokeGradle() throws IOException, BuildSystemException {
        GradleRunner runner = mock(GradleRunner.class, new TriesToReturnSelf());
        when(runner.build()).thenReturn(buildResult(buildTask(":sentinelReport")));

        SentinelGradleBuildReaderAdapter analyzer = new SentinelGradleBuildReaderAdapter(
                mock(SentinelReportParser.class),
                () -> runner);

        // we want to run this test on src/test/resources/sample/
        Path sample = Paths.get(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource("sample/")).getPath());
        assertThat(sample).exists();

        BuildResult result = analyzer.runSentinelReports(sample);
        assertThat(result)
                .isNotNull()
                .satisfies(r -> assertThat(r.getTasks())
                        .hasSize(1)
                        .allMatch(task -> task.getOutcome().equals(TaskOutcome.SUCCESS))
                        .allMatch(task -> task.getPath().equalsIgnoreCase(":sentinelReport"))
                );

    }

    static BuildResult buildResult(BuildTask task) {
        return new DefaultBuildResult("", Collections.singletonList(task));
    }

    static BuildTask buildTask(String path) {
        return buildTask(path, TaskOutcome.SUCCESS);
    }

    static BuildTask buildTask(String path, TaskOutcome outcome) {
        return new DefaultBuildTask(path, outcome);
    }

    @Test
    public void verifyReportParserIsCalled() {
        Report report = Report.builder()
                .project(Project.builder().projectDir(".").build())
                .build();
        SentinelReportParser parser = mock(SentinelReportParser.class);
        when(parser.parse(any(Path.class))).thenReturn(report);

        SentinelGradleBuildReaderAdapter analyzer = new SentinelGradleBuildReaderAdapter(parser, GradleRunner::create);

        // we want to run this test on src/test/resources/sample/
        Path sample = Paths.get(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource("sample/")).getPath());
        assertThat(sample).exists();

        Collection<Report> reports = analyzer.collectSentinelReports(sample);
        assertThat(reports).hasSize(1);

        verify(parser, atLeast(1)).parse(any(Path.class));
    }


}