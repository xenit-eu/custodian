package eu.xenit.custodian.adapters.analysis.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.analysis.gradle.asserts.GradleDependencyContainerAsserts;
import eu.xenit.custodian.adapters.analysis.gradle.build.ExternalModuleIdentifier;
import eu.xenit.custodian.adapters.analysis.gradle.build.GradleBuild;
import eu.xenit.custodian.adapters.analysis.gradle.build.GradleModuleDependency;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Configuration;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Dependency;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Repository;
import eu.xenit.custodian.adapters.analysis.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Gradle;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Project;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Report;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

public class SentinelReportsToGradleBuildConverterTest {

    @Test
    public void convert() {
        SentinelReportsToGradleBuildConverter converter = new SentinelReportsToGradleBuildConverter();

        Collection<Report> reports = Arrays.asList(
                Report.builder()
                        .project(rootProject("parent", subProject("child")))
                        .gradle(defaultGradle())
                        .repositories(defaultRepositories())
                        .configuration("compileClasspath", configuration(Dependencies.apacheHttpClient()))
                        .configuration("testCompileClasspath",
                                configuration(Dependencies.apacheHttpClient(), Dependencies.junit()))
                        .build(),
                Report.builder()
                        .project(subProject("child"))
                        .gradle(defaultGradle())
                        .repositories(defaultRepositories())
                        .configuration("compileClasspath", configuration(Dependencies.apacheHttpClient()))
                        .build()
        );

        GradleBuild gradleBuild = converter.convert(reports);
        assertThat(gradleBuild)
                .isNotNull()
                .satisfies(gradle -> assertThat(gradle.buildsystem().id()).isEqualTo(GradleBuildSystem.ID))
                .satisfies(gradle -> assertThat(gradle.name()).isEqualTo("parent"))
                .satisfies(gradle -> assertThat(gradle.subprojects())
                        .hasSize(1)
                        .hasOnlyOneElementSatisfying(subProject -> assertThat(subProject)
                                .satisfies(sub -> assertThat(sub.name()).isEqualTo("child")))
                )
                .satisfies(gradle -> assertThat(gradle.dependencies().stream().collect(Collectors.toList()))
                                .hasSize(2)
                                .matches(atLeastOneElement(d -> {
                                    return d.getId().equals(Dependencies.junit());
                                }))
                )
                .satisfies(gradle -> GradleDependencyContainerAsserts.assertThat(gradle.dependencies())
                        .isNotNull()
                );


    }

    private static <T> Predicate<List<? extends T>> atLeastOneElement(Predicate<T> predicate) {
        return list -> list.stream().anyMatch(predicate);
    }

    private static Project rootProject(String name, Project... subprojects) {
        return Project.builder()
                .name(name)
                .path(":")
                .projectDir("")
                .subprojects(Stream.of(subprojects).collect(Collectors.toMap(Project::getPath, Function.identity())))
                .build();
    }

    private static Project subProject(String name) {
        return Project.builder().name(name).path(":" + name).projectDir(name).build();
    }

    private static Gradle defaultGradle() {
        return defaultGradle("5.6");
    }

    private static Gradle defaultGradle(String version) {
        return new Gradle(version);
    }

    private static List<Repository> repositories(Repository... repositories) {
        return Arrays.asList(repositories);
    }

    private static Configuration configuration(String... dependenciesNotation) {
        Dependency[] dependencies = Stream.of(dependenciesNotation)
                .map(SentinelReportsToGradleBuildConverterTest::dependency)
                .toArray(Dependency[]::new);
        return configuration(dependencies);
    }

    private static Configuration configuration(ExternalModuleIdentifier... dependencies) {
        return configuration(Arrays.stream(dependencies).map(Dependency::from).toArray(Dependency[]::new));
    }
    private static Configuration configuration(Dependency... dependencies) {
        return Configuration.builder()
                .dependencies(Arrays.asList(dependencies))
                .build();
    }

    private static Dependency dependency(String dependency) {
        return Dependency.from(dependency);
    }

    private static List<Repository> defaultRepositories() {
        return Collections.singletonList(Repository.mavenCentral());
    }
}