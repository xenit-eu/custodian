package eu.xenit.custodian.sentinel.reporting;

import eu.xenit.custodian.sentinel.analyzer.dependencies.AnalyzedDependency;
import eu.xenit.custodian.sentinel.analyzer.dependencies.ConfigurationContainer;
import eu.xenit.custodian.sentinel.analyzer.dependencies.ConfigurationResult;
import eu.xenit.custodian.sentinel.analyzer.dependencies.DependencyContainer;
import eu.xenit.custodian.sentinel.analyzer.dependencies.DependencyResolution;
import eu.xenit.custodian.sentinel.analyzer.dependencies.DependencyResolution.DependencyResolutionState;
import eu.xenit.custodian.sentinel.analyzer.project.ProjectInformation;
import eu.xenit.custodian.sentinel.analyzer.SentinelAnalysisResult;
import eu.xenit.custodian.sentinel.asserts.ArrayNodeAssert;
import eu.xenit.custodian.sentinel.asserts.JsonNodeAssert;
import eu.xenit.custodian.sentinel.asserts.SentinelReportAssert;
import eu.xenit.custodian.sentinel.asserts.condition.Dependency;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.ComponentSelectionReasons;
import org.junit.Test;

public class SentinelJsonReporterTest {

    private SentinelReporter reporter = new SentinelJsonReporter();
//    private SentinelReporter reporter = new SentinelJacksonReporter();

    @Test
    public void empty() throws IOException {

        SentinelAnalysisResult result = SentinelAnalysisResult.builder().build();

        String json = writeReport(result);
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(json);

        report
                .assertField("repositories", repositories -> {
                    repositories.isArray(ArrayNodeAssert::isEmpty);
                })
                .assertField("configurations", configurations -> {
                    configurations.isObject();
                    configurations.isEmpty();
                });
    }

    @Test
    public void projectInfo() throws IOException {
        ProjectInformation info = ProjectInformation.builder()
                .name("foo")
                .path(":foo")
                .subprojects(
                        Stream.of(
                                ProjectInformation.builder().name("bar").path(":foo:bar").build()
                        ).collect(Collectors.toMap(ProjectInformation::getName, Function.identity()))
                )
                .build();
        SentinelAnalysisResult result = SentinelAnalysisResult.builder().project(info).build();

        String json = writeReport(result);
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(json);

        report.assertField("project", project -> {
            project.assertField("subprojects", subprojects -> {
                subprojects.isObject();
                subprojects.assertField("bar", subprojectBar -> {
                    subprojectBar.has("name").has("path");
                    subprojectBar.assertField("subprojects", JsonNodeAssert::isEmpty);
                });
            });
        });
    }

    @Test
    public void testStandardDependency() throws IOException {

        String dependency = "org.apache.httpcomponents:httpclient:4.5.1";
        SentinelAnalysisResult result = result(dependency);

        String json = writeReport(result);
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(json);
        report
                .assertField("repositories", repositories -> {
                    repositories.isArray(ArrayNodeAssert::isEmpty);
                })
                .assertField("configurations", configurations -> {
                    configurations.isObject();
                    configurations.assertField("compileClasspath", compileClasspath -> {
                        compileClasspath.assertFieldArray("dependencies", dependencies -> {
                            dependencies.haveExactlyOne(Dependency.from("org.apache.httpcomponents:httpclient:4.5.1"));
                        });
                    });
                });

    }

    private String writeReport(SentinelAnalysisResult result) throws IOException {
        Writer out = new StringWriter();
        try (IndentingWriter writer = new IndentingWriter(out)) {
            reporter.write(writer, result);
        }
        return out.toString();
    }

    private SentinelAnalysisResult result(String... dependencies) {
        DependencyContainer dependencyContainer = new DependencyContainer();
        Stream.of(dependencies)
                .map(SentinelJsonReporterTest::createResolvedDependency)
                .forEach(dependencyContainer::add);

        ConfigurationContainer configurationContainer = new ConfigurationContainer();
        configurationContainer.add(
                ConfigurationResult.builder().name("compileClasspath").dependencies(dependencyContainer).build()
        );

        return SentinelAnalysisResult.builder().configurations(configurationContainer).build();
    }

    private static AnalyzedDependency createResolvedDependency(String dependencyNotation) {
        AnalyzedDependency result = AnalyzedDependency.from(dependencyNotation);
        result.setResolution(DependencyResolution.builder()
                .state(DependencyResolutionState.RESOLVED)
                .selected(DefaultModuleVersionIdentifier.newId(result, result.getVersion()))
                .reason(ComponentSelectionReasons.requested())
                .build());

        return result;
    }
}