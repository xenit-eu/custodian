package eu.xenit.custodian.sentinel;

import static eu.xenit.custodian.sentinel.asserts.condition.Conditions.dependency;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import eu.xenit.custodian.sentinel.asserts.JsonAssert;
import eu.xenit.custodian.sentinel.asserts.JsonNodeAssert;
import eu.xenit.custodian.sentinel.asserts.SentinelReportAssert;
import eu.xenit.custodian.sentinel.asserts.condition.Conditions;
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

        new SentinelReportAssert(reportFile)
                .assertField("project", project -> {
                    project.isObject();
                    project.assertField("projectDir", "");
                    project.assertField("buildDir", "build");
                    project.assertField("buildFile", "build.gradle");
                })
                .assertField("gradle", gradle -> {
                    gradle.assertField("version", "5.6.2");
                })
                .assertFieldArray("repositories", repositories -> {
                    repositories.hasSize(1);
                    repositories.haveExactlyOne(Repository.withUrl("https://repo.maven.apache.org/maven2/"));
                })
                .assertField("dependencies", dependencies -> {

                    dependencies.isObject();
                    dependencies.assertFieldArray("implementation", implementation -> {
//                        implementation.assertFieldArray("dependencies", dependencies -> {
                        implementation.haveExactlyOne(dependency("org.apache.httpcomponents:httpclient:4.5.1"));
//                            dependencies.doNotHave(Dependency.from("junit:junit:4.12"));
//                        });
                    });

                    dependencies.assertFieldArray("testImplementation", testImplementation -> {
                        testImplementation.haveExactlyOne(dependency("junit:junit:4.12"));
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
                .assertField("dependencies", dependencies -> {
                    dependencies.isNotNull().isObject();
                    dependencies.assertFieldArray("implementation", implementation -> {
                        implementation.haveExactlyOne(dependency("org.springframework.boot:spring-boot-starter"));
                    });
                    dependencies.assertFieldArray("testImplementation", testImplementation -> {
                        testImplementation
                                .haveExactlyOne(dependency("org.springframework.boot:spring-boot-starter-test"));
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
                        subprojects.assertField("subproject-one", s1 -> {
                            s1.assertField("name", "subproject-one");
                            s1.assertField("path", ":subproject-one");
                            s1.assertField("projectDir", "subproject-one");
                            s1.assertField("buildDir", "build");
                            s1.assertField("buildFile", "build.gradle");
                            s1.assertField("subprojects", JsonNodeAssert::isEmpty);
                        });
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
        report.assertField("dependencies", dependencies -> {
            dependencies.assertFieldArray("implementation", implementation -> {
                // currently ignoring special dependencies like 'gradleTestKit()'
                implementation.hasOnlyOneElementSatisfying(dep -> {
                    JsonNodeAssert.assertThat(dep)
                            .assertField("group", "org.apache.httpcomponents")
                            .assertField("artifact", "httpclient")
                            .assertField("version", "4.5.1");
                });
            });
        });
    }
}
