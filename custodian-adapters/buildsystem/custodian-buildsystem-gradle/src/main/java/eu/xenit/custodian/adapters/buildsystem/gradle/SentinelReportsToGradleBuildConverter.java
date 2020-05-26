package eu.xenit.custodian.adapters.buildsystem.gradle;


import eu.xenit.custodian.adapters.buildsystem.gradle.api.RemoteRepository;
import eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto.Dependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto.Report;
import eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto.RepositoryDto;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SentinelReportsToGradleBuildConverter {

    public GradleBuild convert(Path location, Collection<Report> reports, GradleBuildFactory gradleBuildFactory) {
        Map<String, Report> path2report = reports.stream()
                .collect(Collectors.toMap(r -> r.getProject().getPath(), Function.identity()));

        GradleProject rootProject = convert(location, ":", path2report, null);
        return gradleBuildFactory.createGradleBuild(location, rootProject);
    }

    GradleProject convert(Path rootLocation, String name, Map<String, Report> reports, GradleProject parent) {

        Report report = reports.get(name);
        if (report == null) {
            String msg = String.format("No report for project '%s' found: %s", name, reports.keySet());
            throw new IllegalStateException(msg);
        }

        DefaultGradleProject gradleProject = new DefaultGradleProject(
                rootLocation.resolve(report.getProject().getProjectDir()),
                report.getProject().getName(),
                parent);
        gradleProject.setBuildFile(gradleProject.getProjectDir().resolve(report.getProject().getBuildFile()));
        gradleProject.getRepositories().addAll(this.collectRepositories(report.getRepositories()));
        gradleProject.getDependencies().addAll(
                report.getDependencies().stream()
                        .flatMap(entry -> entry.getValue().stream().map(dependency -> {
                            String configuration = entry.getKey();
                            return createGradleModuleDependency(configuration, dependency);
                        })));

//        report.getDependencies()
//        this.collectDependencies(
//                report.getConfigurations(), repoId -> gradleBuild.repositories().get(repoId))
//                .forEach(gradleDep -> gradleBuild.dependencies().add(gradleDep));

        report.getProject().getSubprojects().values().forEach(subProject -> {
            convert(rootLocation, subProject.getPath(), reports, gradleProject);
        });

        return gradleProject;
    }

    private Collection<RemoteRepository> collectRepositories(List<RepositoryDto> repositories) {
        return repositories.stream().map(RemoteRepository::from).collect(Collectors.toList());
    }

//    private List<GradleModuleDependency> collectDependencies(
//            Map<String, Configuration> configurationMap,
//            Function<String, MavenRepository> repoLookup) {
//        return configurationMap.entrySet()
//                .stream()
//                .flatMap(entry -> {
//                    String configurationName = entry.getKey();
//                    Configuration configuration = entry.getValue();
//                    return configuration.getDependencies()
//                            .stream()
//                            .map(d -> createGradleDependency(repoLookup, configurationName, d));
//                })
//                .collect(Collectors.groupingBy(DefaultBackupGradleModuleDependency::getId))
//                .values()
//                .stream()
//                .map(dependenciesById -> dependenciesById.stream()
//                        .reduce((dependency, dependency2) -> {
//                            Builder builder = dependency.toBuilder();
//                            dependency2.getConfigurations().forEach(builder::configuration);
//
//                            return builder.build();
//                        })
//                )
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .peek(dep -> {
//                    String msg = String.format("> %s - configurations: %s", dep.getId(), dep.getConfigurations());
//                    System.out.println(msg);
//                })
//                .collect(Collectors.toList());
//
//    }

//    private GradleModuleDependency createGradleDependency(Function<String, MavenRepository> repoLookup,
//            String configurationName, Dependency d) {

//        String repositoryId = d.getResolution() != null ? d.getResolution().getRepository() : null;
//        Repository repository = repoLookup.apply(repositoryId);
//        if (repository != null) {
//            builder.repository(repository);
//        }
//
//        return builder.build();

    private GradleModuleDependency createGradleModuleDependency(String configurationName, Dependency d) {

        log.warn("TODO - NOT IMPLEMENTED - dropped repo information, resolution state, resolved version, .... !!!");

        return GradleModuleDependency.from(configurationName,
                GradleModuleIdentifier.from(d.getGroup(), d.getArtifact()),
                GradleVersionSpecification.from(d.getVersion()),
                module -> {
                    // TODO what if a module defines multipe artifacts ?!
                    // need to fix that in sentinel first ?
                    module.addArtifact(artifact -> {
                        // TODO most important properties missing ?
//                        artifact.setExtension();
//                        artifact.setName();
                        artifact.setClassifier(d.getClassifier());
                        artifact.setType(d.getType());
                    });
                });
    }
}
