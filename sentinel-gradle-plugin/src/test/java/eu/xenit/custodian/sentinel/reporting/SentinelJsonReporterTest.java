package eu.xenit.custodian.sentinel.reporting;

import static eu.xenit.custodian.sentinel.asserts.JsonNodeAssert.assertThat;

import eu.xenit.custodian.sentinel.adapters.dependencies.AnalyzedDependency;
import eu.xenit.custodian.sentinel.adapters.dependencies.DeclaredModuleDependency;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependenciesAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependenciesContainer;
import eu.xenit.custodian.sentinel.adapters.dependencies.ConfigurationDependenciesContainer;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependenciesContribution;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependencyResolution;
import eu.xenit.custodian.sentinel.adapters.dependencies.DependencyResolution.DependencyResolutionState;
import eu.xenit.custodian.sentinel.adapters.dependencies.JsonDependenciesReporter;
import eu.xenit.custodian.sentinel.adapters.project.ProjectInfoAnalysisContributor;
import eu.xenit.custodian.sentinel.adapters.project.ProjectInformation;
import eu.xenit.custodian.sentinel.asserts.JsonAssert;
import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import eu.xenit.custodian.sentinel.domain.AnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultAnalysisContribution;
import eu.xenit.custodian.sentinel.domain.DefaultReportingFormattingFactory;
import eu.xenit.custodian.sentinel.domain.OutputFormat;
import eu.xenit.custodian.sentinel.domain.SentinelAnalysisReport;
import eu.xenit.custodian.sentinel.asserts.JsonNodeAssert;
import eu.xenit.custodian.sentinel.asserts.SentinelReportAssert;
import eu.xenit.custodian.sentinel.asserts.condition.Conditions;
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
                                ).collect(
                                        Collectors.toMap(ProjectInformation::getName, Function.identity()))
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

        SentinelAnalysisReport result = report(dependencies(
                implementation("org.apache.httpcomponents:httpclient:4.5.1")
        ));

        String json = writeReport(result);
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(json);
        report.assertField("dependencies", dependencies -> {
            dependencies.isObject();
            dependencies.assertFieldArray("implementation", implementation -> {
                implementation.haveExactlyOne(Conditions.dependency("org.apache.httpcomponents:httpclient:4.5.1"));
            });
        });
    }

    @Test
    public void testBomManagedDependency() throws IOException {
        SentinelAnalysisReport report = report(
                dependencies(implementation("org.springframework.boot:spring-boot-starter")));

        String json = writeReport(report);
        System.out.println(json);

        new SentinelReportAssert(json)
                .assertField("dependencies", dependencies -> {
                    dependencies.isObject();
                    dependencies.assertFieldArray("implementation", implementation -> {
                        implementation.haveExactlyOne(
                                Conditions.dependency("org.springframework.boot", "spring-boot-starter", null));
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

    private static AnalysisContribution<DependenciesContainer> dependencies(
            ConfigurationDependenciesContainer... configurations) {
        return new DependenciesContribution(new DependenciesContainer(Stream.of(configurations)));
    }

    private static ConfigurationDependenciesContainer implementation(String... dependencies) {
        return configuration("implementation", dependencies);
    }

    private static ConfigurationDependenciesContainer configuration(String name, String... dependencies) {
        return new ConfigurationDependenciesContainer(
                name, Stream.of(dependencies).map(DeclaredModuleDependency::from));
    }


    @SafeVarargs
    private static SentinelAnalysisReport report(AnalysisContribution<? extends AnalysisContentPart>... contributions) {

        SentinelAnalysisReport report = new SentinelAnalysisReport();
        Arrays.asList(contributions).forEach(contribution -> {
            report.add(contribution.getName(), contribution);
        });

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