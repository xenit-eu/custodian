package eu.xenit.custodian.sentinel.reporting;

import eu.xenit.custodian.sentinel.adapters.dependencies.AnalyzedDependency;
import eu.xenit.custodian.sentinel.adapters.dependencies.ConfigurationContainer;
import eu.xenit.custodian.sentinel.adapters.dependencies.ConfigurationResult;
import eu.xenit.custodian.sentinel.adapters.dependencies.ConfigurationsAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependencyContainer;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependencyResolution;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependencyResolution.DependencyResolutionState;
import eu.xenit.custodian.sentinel.adapters.project.ProjectInfoAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.project.ProjectInformation;
import eu.xenit.custodian.sentinel.asserts.JsonAssert;
import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisReport;
import eu.xenit.custodian.sentinel.asserts.JsonNodeAssert;
import eu.xenit.custodian.sentinel.asserts.SentinelReportAssert;
import eu.xenit.custodian.sentinel.asserts.condition.Dependency;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.ComponentSelectionReasons;
import org.junit.Test;

public class SentinelJsonReporterTest {

    private SentinelReporter reporter = new SentinelJsonReporter();

    @Test
    public void projectInfo() throws IOException {
        ProjectInfoAnalysisContributor projectContributor = new ProjectInfoAnalysisContributor((project) ->
                ProjectInformation.builder()
                        .name("foo")
                        .path(":")
                        .subprojects(
                                Stream.of(
                                        ProjectInformation.builder().name("bar").path(":bar").build()
                                ).collect(Collectors.toMap(ProjectInformation::getName, Function.identity()))
                        )
                        .build());

        SentinelAnalysisReport result = new SentinelAnalysisReport();
        result.add("project", projectContributor.analyze(null));

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
        SentinelAnalysisReport result = report(configurations(dependency));

        String json = writeReport(result);
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(json);
        report
                .assertField("configurations", configurations -> {
                    configurations.isObject();
                    configurations.assertField("compileClasspath", compileClasspath -> {
                        compileClasspath.assertFieldArray("dependencies", dependencies -> {
                            dependencies.haveExactlyOne(Dependency.from("org.apache.httpcomponents:httpclient:4.5.1"));
                        });
                    });
                });
    }

    @Test
    public void testBomManagedDependency() throws IOException {

        AnalyzedDependency result = AnalyzedDependency.from("org.springframework.boot:spring-boot-starter");
        result.setResolution(DependencyResolution.builder()
                .state(DependencyResolutionState.RESOLVED)
                .selected(DefaultModuleVersionIdentifier.newId(result, "5.5.5"))
                .reason(ComponentSelectionReasons.requested())
                .build());

        SentinelAnalysisReport report = report(configurations(result));

        String json = writeReport(report);
        System.out.println(json);

        new SentinelReportAssert(json)
                .assertField("configurations", configurations -> {
                    configurations.isObject();
                    configurations.assertField("compileClasspath", compileClasspath -> {
                        compileClasspath.assertFieldArray("dependencies", dependencies -> {
                            dependencies.satisfies(array -> JsonNodeAssert.assertThat(array.get(0))
                                    .assertField("group", "org.springframework.boot")
                                    .assertField("artifact", "spring-boot-starter")
                                    .assertField("version", JsonAssert::isNullValue)
                            );
                        });
                    });
                });

    }

    private String writeReport(SentinelAnalysisReport result) throws IOException {
        Writer out = new StringWriter();
        try (IndentingWriter writer = new IndentingWriter(out)) {
            reporter.write(writer, result);
        }
        return out.toString();
    }

    private AnalysisContribution<ConfigurationContainer> configurations(AnalyzedDependency... dependencies) {
        ConfigurationsAnalysisContributor contributor = new ConfigurationsAnalysisContributor((project) -> {
            DependencyContainer dependencyContainer = new DependencyContainer();
            Arrays.asList(dependencies)
                    .forEach(dependencyContainer::add);

            ConfigurationContainer configurationContainer = new ConfigurationContainer();
            configurationContainer.add(
                    ConfigurationResult.builder().name("compileClasspath").dependencies(dependencyContainer).build()
            );

            return configurationContainer;
        });

        return contributor.analyze(null);
    }

    private AnalysisContribution<ConfigurationContainer> configurations(String... dependencies) {
        return configurations(Stream.of(dependencies)
                .map(SentinelJsonReporterTest::createResolvedDependency)
                .toArray(AnalyzedDependency[]::new));
    }


    private SentinelAnalysisReport report(AnalysisContribution<ConfigurationContainer> configurations) {

        SentinelAnalysisReport report = new SentinelAnalysisReport();
        report.add("configurations", configurations);

        return report;
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