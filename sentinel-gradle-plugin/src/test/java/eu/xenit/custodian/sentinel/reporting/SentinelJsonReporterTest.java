package eu.xenit.custodian.sentinel.reporting;

import eu.xenit.custodian.sentinel.analyzer.model.AnalyzedDependency;
import eu.xenit.custodian.sentinel.analyzer.model.ConfigurationContainer;
import eu.xenit.custodian.sentinel.analyzer.model.ConfigurationResult;
import eu.xenit.custodian.sentinel.analyzer.model.DependencyContainer;
import eu.xenit.custodian.sentinel.analyzer.model.DependencyResolution;
import eu.xenit.custodian.sentinel.analyzer.model.DependencyResolution.DependencyResolutionState;
import eu.xenit.custodian.sentinel.analyzer.model.RepositoriesContainer;
import eu.xenit.custodian.sentinel.analyzer.model.SentinelAnalysisResult;
import eu.xenit.custodian.sentinel.asserts.ArrayNodeAssert;
import eu.xenit.custodian.sentinel.asserts.SentinelReportAssert;
import eu.xenit.custodian.sentinel.asserts.condition.Dependency;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import org.gradle.api.internal.artifacts.DefaultModuleVersionIdentifier;
import org.gradle.api.internal.artifacts.ivyservice.resolveengine.result.ComponentSelectionReasons;
import org.junit.Test;

public class SentinelJsonReporterTest {

    private SentinelJsonReporter reporter = new SentinelJsonReporter();

    @Test
    public void empty() throws IOException {

        SentinelAnalysisResult result = new SentinelAnalysisResult(
                new ConfigurationContainer(),
                new RepositoriesContainer()
        );

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

    private SentinelAnalysisResult result(String... dependencies)
    {
        DependencyContainer dependencyContainer = new DependencyContainer();
        Stream.of(dependencies)
                .map(SentinelJsonReporterTest::createResolvedDependency)
                .forEach(dependencyContainer::add);

        ConfigurationContainer configurationContainer = new ConfigurationContainer();
        configurationContainer.add(
                ConfigurationResult.builder().name("compileClasspath").dependencies(dependencyContainer).build()
        );

        return new SentinelAnalysisResult(
                configurationContainer,
                new RepositoriesContainer()
        );
    }

    private static AnalyzedDependency createResolvedDependency(String dependencyNotation)
    {
        AnalyzedDependency result = AnalyzedDependency.from(dependencyNotation);
        result.setResolution(DependencyResolution.builder()
                .state(DependencyResolutionState.RESOLVED)
                .selected(DefaultModuleVersionIdentifier.newId(result, result.getVersion()))
                .reason(ComponentSelectionReasons.requested())
                .build());

        return result;
    }
}