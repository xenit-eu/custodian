package eu.xenit.custodian.adapters.buildsystem.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.domain.buildsystem.GradleBuildAssert;
import eu.xenit.custodian.adapters.buildsystem.gradle.notation.GradleNotationFormatter;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.DependencySet;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.Dependencies;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.Dependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.DependencyFixtures;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.Gradle;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.Project;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.Report;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.RepositoryDto;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
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
                        .dependencies(dependencies(
                                implementation(DependencyFixtures.apacheHttpClient()),
                                testImplementation(DependencyFixtures.junit())
                        ))
                        .build(),
                Report.builder()
                        .project(subProject("child"))
                        .gradle(defaultGradle())
                        .repositories(defaultRepositories())
                        .dependencies(dependencies(
                                implementation(DependencyFixtures.apacheHttpClient())
                        ))
                        .build()
        );

        GradleBuild gradleBuild = converter.convert(Paths.get("."), reports);

        GradleBuildAssert.assertThat(gradleBuild)
                .isNotNull()
                .isGradleBuild()
                .hasName("parent")

                .getRootProject()
                .hasDependency(GradleNotationFormatter.format(MavenModuleIdentifierFixtures.junit()))

                .withChildProjects(subprojects -> assertThat(subprojects.iterator())
                        .hasSize(1)
                        .hasOnlyOneElementSatisfying(subProject -> assertThat(subProject.getName()).isEqualTo("child")))
        ;

    }

    private static <T> Predicate<List<? extends T>> atLeastOneElement(Predicate<T> predicate) {
        return list -> list.stream().anyMatch(predicate);
    }

    private static Project rootProject(String name, Project... subprojects) {
        return Project.builder()
                .name(name)
                .path(":")
                .projectDir("")
                .buildFile("build.gradle")
                .buildDir("build")
                .subprojects(Stream.of(subprojects).collect(Collectors.toMap(Project::getPath, Function.identity())))
                .build();
    }

    private static Project subProject(String name) {
        return Project.builder()
                .name(name)
                .path(":" + name)
                .projectDir(name)
                .buildFile("build.gradle")
                .buildDir("build")
                .build();
    }

    private static Gradle defaultGradle() {
        return defaultGradle("5.6");
    }

    private static Gradle defaultGradle(String version) {
        return new Gradle(version);
    }

    private static List<RepositoryDto> repositories(RepositoryDto... repositories) {
        return Arrays.asList(repositories);
    }

    private static Dependencies dependencies(ConfigurationDependencySet... dependencies) {
        Stream<Entry<String, DependencySet>> entryStream = Stream.of(dependencies).map(set -> set.entry());
        return new Dependencies(entryStream);
    }

    private static ConfigurationDependencySet dependencySet(String configuration, Dependency... dependencies) {
        return new ConfigurationDependencySet(configuration, new DependencySet(dependencies));
    }

    private static ConfigurationDependencySet implementation(Dependency... dependencies) {
        return dependencySet("implementation", dependencies);
    }

    private static ConfigurationDependencySet testImplementation(Dependency... dependencies) {
        return dependencySet("testImplementation", dependencies);
    }


    private static Dependency dependency(String dependency) {
        return Dependency.from(dependency);
    }

    private static List<RepositoryDto> defaultRepositories() {
        return Collections.singletonList(RepositoryDto.mavenCentral());
    }

    @Data
    static class ConfigurationDependencySet {

        private final String configuration;
        private final DependencySet dependencySet;

        public Map.Entry<String, DependencySet> entry() {
            return new SimpleEntry<>(configuration, dependencySet);
        }
    }
}