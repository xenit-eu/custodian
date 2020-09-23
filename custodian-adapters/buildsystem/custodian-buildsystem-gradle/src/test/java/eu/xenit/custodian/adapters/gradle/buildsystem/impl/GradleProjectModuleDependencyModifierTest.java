package eu.xenit.custodian.adapters.gradle.buildsystem.impl;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.gradle.buildsystem.Fixtures;
import eu.xenit.custodian.adapters.gradle.buildsystem.Fixtures.Dependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.Fixtures.Modules;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleBuildFileAssert;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleBuildFileAssertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GradleProjectModuleDependencyModifierTest {

    @Test
    void tryUpdateInlineDependency(@TempDir Path dir) throws IOException {

        GradleModuleDependency apacheHttpClient = Fixtures.Dependency.apacheHttpClient("4.5.0");
        Path buildFile = new GradleBuildSystem().builder()
                .addProject(project -> project
                        .withJavaPlugin()
                        .withMavenCentral()
                        .withDependencies(dependencies -> dependencies.add(apacheHttpClient))
                )
                .materialize(dir)
                .getRootProject()
                .getBuildFile();

        String buildGradleSource = Files.readString(buildFile);

        GradleBuildFileAssertions.assertThat(buildGradleSource)
                .hasDependency(apacheHttpClient);

        String result = GradleProjectModuleDependencyModifier.updateInlineDependencyVersion(
                apacheHttpClient, "4.5.1", buildGradleSource);

        GradleBuildFileAssertions.assertThat(result)
                .hasDependency(Fixtures.Dependency.apacheHttpClient("4.5.1"));


    }

    @Test
    void updateVersionSingleQuotedDependency() {
        String source = String.join("\n",
                "dependencies {",
                "    implementation 'org.apache.httpcomponents:httpclient:4.5.0'",
                "}");

        GradleModuleDependency dependency = Fixtures.Dependency.apacheHttpClient("4.5.0");
        String result = GradleProjectModuleDependencyModifier.updateInlineDependencyVersion(
                dependency, "4.5.1", source);

        assertThat(result).contains("implementation 'org.apache.httpcomponents:httpclient:4.5.1'");
    }

    @Test
    void updateVersionDoubleQuotedDependency() {
        String source = String.join("\n",
                "dependencies {",
                "    implementation \"org.apache.httpcomponents:httpclient:4.5.0\"",
                "}");

        GradleModuleDependency dependency = Fixtures.Dependency.apacheHttpClient("4.5.0");
        String result = GradleProjectModuleDependencyModifier.updateInlineDependencyVersion(
                dependency, "4.5.1", source);

        assertThat(result).contains("implementation \"org.apache.httpcomponents:httpclient:4.5.1\"");
    }

    @Test
    void updateVersionMethodInvocationDependency() {
        String source = String.join("\n",
                "dependencies {",
                "    implementation('org.apache.httpcomponents:httpclient:4.5.0')",
                "}");

        GradleModuleDependency dependency = Fixtures.Dependency.apacheHttpClient("4.5.0");
        String result = GradleProjectModuleDependencyModifier.updateInlineDependencyVersion(
                dependency, "4.5.1", source);

        assertThat(result).contains("implementation('org.apache.httpcomponents:httpclient:4.5.1')");
    }
}