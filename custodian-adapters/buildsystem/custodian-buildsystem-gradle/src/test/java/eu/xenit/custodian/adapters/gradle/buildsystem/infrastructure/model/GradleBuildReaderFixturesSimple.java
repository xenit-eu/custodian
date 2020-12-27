package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleBuildAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.toolingapi.GradleToolingBuildReaderAdapter;
import eu.xenit.custodian.adapters.gradle.buildsystem.spi.model.GradleBuildReaderPort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.gradle.groovy.scripts.StringScriptSource;
import org.gradle.plugin.use.internal.PluginRequestCollector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class GradleBuildReaderFixturesSimple {

    //    static class GradleBuilderReaderProvider implements ArgumentsProvider {
//
//        @Override
//        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
//            return Stream.of(
////                    Arguments.of(new SentinelGradleBuildReaderAdapter(
////                            new JacksonSentinelReportParser(), GradleRunner::create
////                    )),
//                    Arguments.of(new GradleToolingBuildReaderAdapter())
//            );
//        }
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(GradleBuilderReaderProvider.class)
//    void testSimpleFixture(GradleBuildReaderPort reader) throws BuildSystemException {

    @Test
    void testPluginsFromScriptSource() throws IOException {
        Path location = Paths.get("src/test/resources/fixtures/simple/build.gradle");
        String content = Files.readString(location);
        StringScriptSource source = new StringScriptSource("build.gradle", content);
        PluginRequestCollector pluginRequestCollector = new PluginRequestCollector(source);
    }

    @Test
    void testSimpleFixture() throws BuildSystemException {
        GradleBuildReaderPort reader = new GradleToolingBuildReaderAdapter();
        Path location = Paths.get("src/test/resources/fixtures/simple");
        assertThat(location).exists().isDirectory();

        GradleBuild build = reader.getBuild(location).orElseThrow();

        GradleBuildAssert.assertThat(build)
                .assertRootProject(project -> {
                    project.hasJavaPlugin();
                });

//        new SentinelReportAssert(reportFile)
//                .assertField("project", project -> {
//                    project.isObject();
//                    project.assertField("projectDir", "");
//                    project.assertField("buildDir", "build");
//                    project.assertField("buildFile", "build.gradle");
//                })
//                .assertField("gradle", gradle -> {
//                    gradle.assertField("version", "5.6.2");
//                })
//                .assertFieldArray("repositories", repositories -> {
//                    repositories.hasSize(1);
//                    repositories.haveExactlyOne(Repository.withUrl("https://repo.maven.apache.org/maven2/"));
//                })
//                .assertField("dependencies", dependencies -> {
//
//                    dependencies.isObject();
//                    dependencies.assertFieldArray("implementation", implementation -> {
////                        implementation.assertFieldArray("dependencies", dependencies -> {
//                        implementation.haveExactlyOne(dependency("org.apache.httpcomponents:httpclient:4.5.1"));
////                            dependencies.doNotHave(Dependency.from("junit:junit:4.12"));
////                        });
//                    });
//
//                    dependencies.assertFieldArray("testImplementation", testImplementation -> {
//                        testImplementation.haveExactlyOne(dependency("junit:junit:4.12"));
//                    });
//                });
    }

    @Test
    void testSpringBootStarterFixture() throws BuildSystemException {
        GradleBuildReaderPort reader = new GradleToolingBuildReaderAdapter();
        Path location = Paths.get("src/test/resources/fixtures/springboot/starter");
        assertThat(location).exists().isDirectory();

        GradleBuild build = reader.getBuild(location).orElseThrow();

        GradleBuildAssert.assertThat(build)
                .assertRootProject(project -> {
                    project.hasJavaPlugin();
                });
    }

}
