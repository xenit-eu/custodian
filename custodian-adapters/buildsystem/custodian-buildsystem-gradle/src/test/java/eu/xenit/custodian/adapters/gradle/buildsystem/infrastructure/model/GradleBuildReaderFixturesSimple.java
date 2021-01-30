package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleBuildAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.toolingapi.GradleToolingBuildReaderAdapter;
import eu.xenit.custodian.adapters.gradle.buildsystem.spi.model.GradleBuildReaderPort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

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
    void testSimpleFixture() throws BuildSystemException {
        GradleBuildReaderPort reader = new GradleToolingBuildReaderAdapter();
        Path location = Paths.get("src/test/resources/fixtures/simple");
        assertThat(location).exists().isDirectory();

        GradleBuild build = reader.getBuild(location).orElseThrow();

        GradleBuildAssert.assertThat(build)
                .assertRootProject(project -> {
                    project.hasName("simple");
                    project.hasProjectDir(location);
                    project.hasJavaPlugin();

                    project.hasMavenCentralRepository();

                    project.hasImplementationDependency("org.apache.httpcomponents:httpclient:4.5.1");
                    project.hasTestImplementationDependency("junit:junit:4.12");
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
                    project.hasName("starter");
                    project.hasGroup("com.example");
                    project.hasVersion("0.0.1-SNAPSHOT");
                    // sourceCompatibility = '11'

                    project.hasJavaPlugin();
                    project.hasPlugin("org.springframework.boot", "2.4.2");
                    project.hasPlugin("io.spring.dependency-management", "1.0.11.RELEASE");

                    project.assertDependencies(dependencies -> {
                        dependencies.hasImplementation("org.springframework.boot:spring-boot-starter-security");
                        dependencies.hasImplementation("org.springframework.boot:spring-boot-starter-web");

                        dependencies.hasTestImplementation("org.springframework.boot:spring-boot-starter-test");
                        dependencies.hasTestImplementation("org.springframework.security:spring-security-test");
                    });
                });
    }


}
