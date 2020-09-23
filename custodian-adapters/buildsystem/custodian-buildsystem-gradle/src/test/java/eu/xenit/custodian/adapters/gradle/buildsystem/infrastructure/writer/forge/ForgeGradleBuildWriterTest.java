package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.Fixtures.Modules;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleBuildFileAssertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ForgeGradleBuildWriterTest {

    @Test
    void writeProjectRecursively() throws IOException {
        ForgeGradleBuildWriter forge = new ForgeGradleBuildWriter();

        GradleBuild build = new GradleBuildSystem().builder()
                .addProject(project -> project
                        .name("root")
                        .withJavaPlugin()
                        .withMavenCentral()
                        .withDependencies(dependencies -> {
                            dependencies.implementation(Modules.apacheHttpClient(), "4.5.6");
                        })
                )
                .build();

        Path buildDotGradle = forge.writeBuildDotGradle(build.getRootProject());

        String actual = Files.readString(buildDotGradle);
        System.out.println(actual);

        assertThat(buildDotGradle).isRegularFile();

        GradleBuildFileAssertions.assertThat(actual)
                .hasJavaPlugin()
                .hasMavenCentralRepository()
                .hasDependency("implementation", "org.apache.httpcomponents:httpclient:4.5.6");

    }
}