package eu.xenit.custodian.sentinel;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import eu.xenit.custodian.sentinel.asserts.JsonAssert;
import eu.xenit.custodian.sentinel.asserts.JsonNodeAssert;
import eu.xenit.custodian.sentinel.asserts.SentinelReportAssert;
import eu.xenit.custodian.sentinel.asserts.condition.Dependency;
import eu.xenit.custodian.sentinel.asserts.condition.Repository;
import eu.xenit.custodian.sentinel.gradle.testkit.GradleBuild;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Rule;
import org.junit.Test;

public class SentinelIntegrationTests {

    @Rule
    public final GradleBuild gradleBuild = new GradleBuild();


    @Test
    public void testExampleSimple() throws IOException {
        BuildResult result = this.gradleBuild.source("src/test/resources/examples/simple")
                .build("sentinelReport");

        BuildTask sentinelReportTask = result.task(":" + SentinelReportTask.TASK_NAME);
        assertThat(sentinelReportTask, is(notNullValue()));
        assertThat(sentinelReportTask.getOutcome(), is(TaskOutcome.SUCCESS));

        File reportFile = new File(this.gradleBuild.getProjectDir(), "build/sentinel/report.json");

        // debug output
        String json = new String(Files.readAllBytes(reportFile.toPath()));
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(reportFile);
        report
                .assertField("project", JsonAssert::isObject)
                .assertField("gradle", gradle -> {
                    gradle.assertField("version", "5.6");
                    gradle.assertField("buildDir", "build");
                    gradle.assertField("buildFile", "build.gradle");
                })
                .assertFieldArray("repositories", repositories -> {
                    repositories.hasSize(1);
                    repositories.haveExactlyOne(Repository.withUrl("https://repo.maven.apache.org/maven2/"));
                })
                .assertField("configurations", configurations -> {

                    configurations.isObject();
                    configurations.assertField("compileClasspath", compileClasspath -> {
                        compileClasspath.assertFieldArray("dependencies", dependencies -> {
                            dependencies.haveExactlyOne(Dependency.from("org.apache.httpcomponents:httpclient:4.5.1"));
                            dependencies.doNotHave(Dependency.from("junit:junit:4.12"));
                        });
                    });

                    configurations.assertField("testRuntimeClasspath", testRuntimeClasspath -> {
                        testRuntimeClasspath.assertFieldArray("dependencies", dependencies -> {
                            dependencies.haveExactlyOne(Dependency.from("org.apache.httpcomponents:httpclient:4.5.1"));
                            dependencies.haveExactlyOne(Dependency.from("junit:junit:4.12"));
                        });
                    });
                });
    }

    @Test
    public void testSpringBootStarter() throws IOException {
        BuildResult result = this.gradleBuild.source("src/test/resources/examples/springboot/starter")
                .build("sentinelReport");

        BuildTask sentinelReportTask = result.task(":" + SentinelReportTask.TASK_NAME);
        assertThat(sentinelReportTask, is(notNullValue()));
        assertThat(sentinelReportTask.getOutcome(), is(TaskOutcome.SUCCESS));

        File reportFile = new File(this.gradleBuild.getProjectDir(), "build/sentinel/report.json");

        // debug output
        String json = new String(Files.readAllBytes(reportFile.toPath()));
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(reportFile);
        report
                .assertFieldArray("repositories", repositories -> {
                    repositories.hasSize(1);
                    repositories.haveExactlyOne(Repository.withUrl("https://repo.maven.apache.org/maven2/"));
                })
                .assertField("configurations", configurations -> {

                    configurations.isObject();
                    configurations.assertField("compileClasspath", compileClasspath -> {
                        compileClasspath.assertFieldArray("dependencies", dependencies -> {
                            dependencies
                                    .haveExactlyOne(Dependency.from("org.springframework.boot:spring-boot-starter"));
                        });
                    });

                    configurations.assertField("testRuntimeClasspath", testRuntimeClasspath -> {
                        testRuntimeClasspath.assertFieldArray("dependencies", dependencies -> {
                            dependencies
                                    .haveExactlyOne(Dependency.from("org.springframework.boot:spring-boot-starter:"));
                            dependencies.haveExactlyOne(
                                    Dependency.from("org.springframework.boot:spring-boot-starter-test:"));
                        });
                    });
                });
    }

    @Test
    public void testSubprojects() throws IOException {
        BuildResult result = this.gradleBuild.source("src/test/resources/examples/subprojects")
                .build("sentinelReport");

        BuildTask sentinelReportTask = result.task(":" + SentinelReportTask.TASK_NAME);
        assertThat(sentinelReportTask, is(notNullValue()));
        assertThat(sentinelReportTask.getOutcome(), is(TaskOutcome.SUCCESS));

        File reportFile = new File(this.gradleBuild.getProjectDir(), "build/sentinel/report.json");

        // debug output
        String json = new String(Files.readAllBytes(reportFile.toPath()));
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(reportFile);
        report
                .assertField("project", project -> {
                    project.assertField("subprojects", subprojects -> {
                        subprojects.has("subproject-one");
                        subprojects.has("subproject-two");
                    });
                });
    }

    @Test
    public void testSpecial() throws IOException {
        BuildResult result = this.gradleBuild.source("src/test/resources/examples/special")
                .build("sentinelReport");

        BuildTask sentinelReportTask = result.task(":" + SentinelReportTask.TASK_NAME);
        assertThat(sentinelReportTask, is(notNullValue()));
        assertThat(sentinelReportTask.getOutcome(), is(TaskOutcome.SUCCESS));

        File reportFile = new File(this.gradleBuild.getProjectDir(), "build/sentinel/report.json");

        // debug output
        String json = new String(Files.readAllBytes(reportFile.toPath()));
        System.out.println(json);

        SentinelReportAssert report = new SentinelReportAssert(reportFile);
        report
                .assertField("compileClasspath", compileClasspath -> {
                    compileClasspath.assertFieldArray("dependencies", dependencies -> {
                        dependencies.satisfies(arrayNode -> arrayNode.forEach(jsonDependency -> {
                            JsonNodeAssert.assertThat(jsonDependency)
                                    .assertField("group", JsonAssert::isNotNull)
                                    .assertField("artifact", artifact -> {
                                        artifact.doesNotHaveTextValue("unspecified");
                                    });

                        }));
                    });
                });
    }
}