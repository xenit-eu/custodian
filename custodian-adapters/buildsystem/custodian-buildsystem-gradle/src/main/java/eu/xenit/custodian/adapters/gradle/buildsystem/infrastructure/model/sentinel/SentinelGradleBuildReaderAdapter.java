package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.spi.model.GradleBuildReaderPort;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto.Project;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto.Report;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.gradle.testkit.runner.UnexpectedBuildFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentinelGradleBuildReaderAdapter implements GradleBuildReaderPort {

    private static final Logger log = LoggerFactory.getLogger(SentinelGradleBuildReaderAdapter.class);

    public static final String SENTINEL_REPORT_OUTPUT = "build/sentinel/custodian-sentinel-report.json";

    private static final String SENTINEL_REPORT_TASK = "sentinelReport";
    public static final String SENTINEL_INIT_GRADLE = "sentinel/sentinel.gradle";

    private final SentinelReportParser parser;
    private final Supplier<GradleRunner> gradleSupplier;

    private final SentinelReportsModelConverter converter = new SentinelReportsModelConverter();

    public SentinelGradleBuildReaderAdapter(SentinelReportParser parser, Supplier<GradleRunner> gradleSupplier) {

        Objects.requireNonNull(gradleSupplier, "gradleSupplier must not be null");
        Objects.requireNonNull(parser, "parser must not be null");

        this.parser = parser;
        this.gradleSupplier = gradleSupplier;
    }

    public Optional<GradleBuild> getBuild(Path location) throws BuildSystemException {
        this.validateLocation(location);

        try {
            // Inject and execute the :sentinelReport task in the gradle project
            this.runSentinelReports(location);

            // Collect and parse all sentinel reports
            Collection<Report> reports = this.collectSentinelReports(location);
            reports.forEach(r -> log.info("> " + r.getProject().getName()));

            // Convert the sentinel reports and a custodian build
            GradleBuild gradle = this.converter.convert(location, reports);
            return Optional.of(gradle);

        } catch (IOException | UnexpectedBuildFailure e) {
            throw new BuildSystemException(e);
        }
    }

    BuildResult runSentinelReports(Path location)
            throws IOException, UnexpectedBuildFailure, BuildSystemException {
        Path sentinelInitPath = null;
        try {
            // SENTINEL_INIT_GRADLE contains a gradle init-script and is located in a .jar file
            sentinelInitPath = Files.createTempFile("sentinel-", ".gradle");
            try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(SENTINEL_INIT_GRADLE)) {
                Objects.requireNonNull(stream, "stream not found: " + SENTINEL_INIT_GRADLE);
                Files.copy(stream, sentinelInitPath, StandardCopyOption.REPLACE_EXISTING);
            }

            GradleRunner runner = this.gradleSupplier.get()
                    .withProjectDir(location.toFile())
                    .forwardOutput()
                    .withArguments("--init-script", sentinelInitPath.toString(), SENTINEL_REPORT_TASK);

            BuildResult buildResult = runner.build();

            List<BuildTask> failedTasks = buildResult.getTasks()
                    .stream()
                    .filter(task -> !hasExpectedOutcome(task))
                    .collect(Collectors.toList());
            if (!failedTasks.isEmpty()) {
                throw new BuildSystemException(
                        "Running sentinelReport failed for tasks: " + failedTasks.toString());
            }

            // optionally we could assert if number of (successful) tasks > 0 ?

            return buildResult;
        } finally {
            if (sentinelInitPath != null) {
                Files.deleteIfExists(sentinelInitPath);
            }
        }
    }

    Collection<Report> collectSentinelReports(Path location) {
        // Parse the root project report and collect all subprojects
        Path rootReportPath = location.resolve(SENTINEL_REPORT_OUTPUT);
        if (!Files.exists(rootReportPath)) {
            return Collections.emptyList();
        }

        Report rootReport = parser.parse(rootReportPath);

        // Flatmap all projects, get the report from each projectdir and parse
        Collection<Report> reports = flatMapSubprojects(rootReport.getProject())
                .map(Project::getProjectDir)
                .map(location::resolve)
                .map(projectDir -> projectDir.resolve(SENTINEL_REPORT_OUTPUT).toAbsolutePath().normalize())
                .peek(reportJson -> System.out.println(reportJson.toString()))
                .peek(reportJson -> log.info(reportJson.toString()))
                .map(parser::parse)
                .collect(Collectors.toList());

        return reports;
    }

    /**
     * Method to collect a project and all subprojects recursively
     *
     * @return a stream of the project and question and all it's subprojects
     */
    private static Stream<Project> flatMapSubprojects(Project project) {
        return Stream.concat(Stream.of(project),
                project.getSubprojects().values().stream().flatMap(SentinelGradleBuildReaderAdapter::flatMapSubprojects));
    }

    private void validateLocation(Path location) {
        Objects.requireNonNull(location, "location can not be null");
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

    private static boolean hasExpectedOutcome(BuildTask task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        List<TaskOutcome> expectedOutcomes = Arrays.asList(TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE);
        return expectedOutcomes.contains(task.getOutcome());
    }

}
