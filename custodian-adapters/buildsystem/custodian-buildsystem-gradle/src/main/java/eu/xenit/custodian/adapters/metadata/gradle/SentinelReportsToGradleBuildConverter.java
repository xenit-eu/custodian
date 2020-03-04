package eu.xenit.custodian.adapters.metadata.gradle;


import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleModuleDependency;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleVersionSpecification;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.RepositoryFactory;
import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.Dependency;
import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.Report;
import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.RepositoryDto;
import eu.xenit.custodian.domain.buildsystem.GroupArtifactModuleIdentifier;
import eu.xenit.custodian.domain.buildsystem.Repository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SentinelReportsToGradleBuildConverter {

    public GradleBuild convert(Collection<Report> reports) {

        Map<String, Report> path2report = reports.stream()
                .collect(Collectors.toMap(r -> r.getProject().getPath(), Function.identity()));

        return convert(":", path2report, null);

    }

    GradleBuild convert(String name, Map<String, Report> reports, GradleBuild parent) {

        Report report = reports.get(name);
        if (report == null) {
            String msg = String.format("No report for project '%s' found: %s", name, reports.keySet());
            throw new IllegalStateException(msg);
        }

        GradleBuild gradleBuild = new GradleBuild(report.getProject().getName(), parent);
        gradleBuild.repositories().addAll(this.collectRepositories(report.getRepositories()));


        Stream<GradleModuleDependency> gradleModuleDependencyStream = report.getDependencies().stream()
                .flatMap(entry -> entry.getValue().stream().map(dependency -> {
                    String configuration = entry.getKey();
                    return createGradleModuleDependency(configuration, dependency);
                }));
        gradleBuild.dependencies().add(gradleModuleDependencyStream);


//        report.getDependencies()
//        this.collectDependencies(
//                report.getConfigurations(), repoId -> gradleBuild.repositories().get(repoId))
//                .forEach(gradleDep -> gradleBuild.dependencies().add(gradleDep));


        report.getProject().getSubprojects().values().forEach(subProject -> {
            convert(subProject.getPath(), reports, gradleBuild);
        });

        return gradleBuild;
    }

    private Collection<Repository> collectRepositories(List<RepositoryDto> repositories) {
        RepositoryFactory factory = new RepositoryFactory();
        return repositories.stream().map(factory::from).collect(Collectors.toList());
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
                GroupArtifactModuleIdentifier.from(d.getGroup(), d.getArtifact()),
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
