package eu.xenit.custodian.adapters.analysis.gradle;

import eu.xenit.custodian.adapters.analysis.gradle.build.GradleBuild;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Project;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Report;
import eu.xenit.custodian.domain.repository.scm.ProjectHandle;
import eu.xenit.custodian.domain.repository.analysis.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.repository.analysis.AnalyzerException;
import eu.xenit.custodian.domain.repository.analysis.ProjectAnalyzer;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

public class SentinelGradleProjectAnalyzer implements ProjectAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(SentinelGradleProjectAnalyzer.class);

    private static final String SENTINEL_REPORT_TASK = "sentinelReport";
    private static final String SENTINEL_REPORT_OUTPUT = "custodian-sentinel-report.json";
    public static final String SENTINEL_INIT_GRADLE = "sentinel/sentinel.gradle";

    private SentinelReportParser parser;
    private final Supplier<GradleRunner> gradleSupplier;

    public SentinelGradleProjectAnalyzer() {
        this(new JacksonSentinelReportParser(), GradleRunner::create);
    }

    public SentinelGradleProjectAnalyzer(SentinelReportParser parser, Supplier<GradleRunner> gradleSupplier) {
        Objects.requireNonNull(gradleSupplier, "gradleSupplier must not be null");
        Objects.requireNonNull(parser, "parser must not be null");

        this.parser = parser;
        this.gradleSupplier = gradleSupplier;
    }

    @Override
    public ProjectMetadata analyze(ProjectHandle handle) throws AnalyzerException {
        Path location = handle.getLocation();
        this.validateLocation(location);

        ProjectMetadata project = new ProjectMetadata(handle.getReference());

        try {
            // Inject and execute the :sentinelReport task in the gradle project
            this.runSentinelReports(location);

            // Collect and parse all sentinel reports
            Collection<Report> reports = this.collectSentinelReports(location);
            reports.forEach(r -> log.info("> " + r.getProject().getName()));

            // Convert the sentinel reports and a custodian build

            GradleBuild gradle = new GradleBuild("foo");
            project.buildsystems().add(gradle);

        } catch (IOException | UnexpectedBuildFailure e) {
            throw new AnalyzerException(e);

        }

        return project;
    }

    BuildResult runSentinelReports(Path location) throws IOException, UnexpectedBuildFailure, AnalyzerException {
        // locate `sentinel.gradle'
        // Might need to change this into .getResourceAsStream() and write to temp file
        // especially if we're running this from a .jar file
        URL sentinelInit = this.getClass().getClassLoader().getResource(SENTINEL_INIT_GRADLE);
        if (sentinelInit == null) {
            throw new IOException("Could not locate "+SENTINEL_INIT_GRADLE);
        }

        GradleRunner runner = this.gradleSupplier.get()
                .withProjectDir(location.toFile())
                // debug = true triggers a bug in gradle
                // see https://github.com/gradle/gradle/issues/3995
                // .withDebug(false)
                .forwardOutput()
                .withArguments("--init-script", sentinelInit.getPath(), SENTINEL_REPORT_TASK);

        BuildResult buildResult = runner.build();

        List<BuildTask> failedTasks = buildResult.getTasks()
                .stream()
                .filter(task -> !hasExpectedOutcome(task))
                .collect(Collectors.toList());
        if (!failedTasks.isEmpty()) {
            throw new AnalyzerException("Running sentinelReport failed for tasks: " + failedTasks.toString());
        }

        // optionally we could assert if number of (successful) tasks > 0 ?

        return buildResult;
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
     * @param project
     * @return a stream of the project and question and all it's subprojects
     */
    private static Stream<Project> flatMapSubprojects(Project project) {
        return Stream.concat(Stream.of(project),
                project.getSubprojects().values().stream().flatMap(SentinelGradleProjectAnalyzer::flatMapSubprojects));
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

    private static boolean hasExpectedOutcome(BuildTask task) {
        if (task == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        List<TaskOutcome> expectedOutcomes = Arrays.asList(TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE);
        return expectedOutcomes.contains(task.getOutcome());
    }
}
