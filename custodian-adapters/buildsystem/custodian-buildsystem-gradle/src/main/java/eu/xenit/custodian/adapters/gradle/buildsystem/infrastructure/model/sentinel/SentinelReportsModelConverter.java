package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel;


import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.builder.GradleBuilder;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto.Report;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto.RepositoryDto.RepositoryType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SentinelReportsModelConverter {

    private GradleBuildSystem gradle = new GradleBuildSystem();

    public GradleBuild convert(Path rootLocation, Collection<Report> reports) {

        GradleBuilder builder = gradle.builder();

//        DefaultGradleProjectTree projectTree = new DefaultGradleProjectTree();
        reports.forEach(report -> {
            this.addGradleProjectReport(builder, rootLocation, report);
        });

        return builder.build();
    }

    private void addGradleProjectReport(GradleBuilder builder, Path root, Report report) {

        builder.addProject(project -> project
            .path(report.getProject().getPath())

            .projectDir(report.getProject().getProjectDir())
            .name(report.getProject().getName())
            .buildFile(report.getProject().getBuildDir())

            .withPlugins(plugins -> {
                report.getPlugins().forEach(p -> {
                    plugins.addPlugin(p.getId(), p.getVersion(), p.isApply());
                });
            })


            .withRepositories(repositories -> {
                report.getRepositories().forEach(r -> {
                    if (r.getType() == RepositoryType.MAVEN) {
                        repositories.addMavenRepository(r.getName(), r.getUrl());
                    }
                });
            })


            .withDependencies(dependencies -> {
                report.getDependencies().stream().forEach(entry -> {
                    entry.getValue().forEach(dep -> {
                        dependencies.add(dep.getGroup(), dep.getArtifact(), dependency -> {
                            dependency.configuration(entry.getKey());
                            dependency.version(dep.getVersion());
                        });
                    });
                });
            })
        );
    }
//
//    private DefaultGradleProjectBuilder convert(Path location, Report report) {
//
//        return GradleProject.builder()
//                .setProjectDir(location.resolve(report.getProject().getProjectDir()))
//                .setName(report.getProject().getName())
//                .setBuildFile(location.resolve(report.getProject().getProjectDir())
//                        .resolve(report.getProject().getBuildFile()))
//                .setPlugins(report.getPlugins().stream()
//                        .map(p -> new GradlePlugin(p.getId(), p.getVersion(), p.isApply()))
//                        .collect(Collectors.toList()))
//                .setRepositories(this.collectRepositories(report.getRepositories()))
//                .setDependencies(report.getDependencies().stream()
//                        .flatMap(entry -> entry.getValue().stream().map(dependency -> {
//                            String configuration = entry.getKey();
//                            return createGradleModuleDependency(configuration, dependency);
//                        })).collect(Collectors.toList()));
//    }

//    GradleProject convert(Path rootLocation, String name, Map<String, Report> reports, GradleProject parent) {
//
//        Report report = reports.get(name);
//        if (report == null) {
//            String msg = String.format("No report for project '%s' found: %s", name, reports.keySet());
//            throw new IllegalStateException(msg);
//        }
//
////        DefaultGradleProject gradleProject = new DefaultGradleProject(
////                rootLocation.resolve(report.getProject().getProjectDir()),
////                report.getProject().getName(),
////                parent);
//
//        Path projectDir = rootLocation.resolve(report.getProject().getProjectDir());
//        GradleProject gradleProject = GradleProject.builder()
//                .setProjectDir(projectDir)
//                .setName(report.getProject().getName())
//                .setParent(parent)
//                .setBuildFile(projectDir.resolve(report.getProject().getBuildFile()))
//                .setPlugins(report.getPlugins().stream()
//                        .map(p -> new GradlePlugin(p.getId(), p.getVersion(), p.isApply()))
//                        .collect(Collectors.toList()))
//                .setRepositories(this.collectRepositories(report.getRepositories()))
//                .setDependencies(report.getDependencies().stream()
//                        .flatMap(entry -> entry.getValue().stream().map(dependency -> {
//                            String configuration = entry.getKey();
//                            return createGradleModuleDependency(configuration, dependency);
//                        })).collect(Collectors.toList()))
//                .setProjectTree(projectTree)
//                .build();
////        gradleProject.setBuildFile(gradleProject.getProjectDir().resolve(report.getProject().getBuildFile()));
////        gradleProject.getRepositories().addAll(this.collectRepositories(report.getRepositories()));
////        gradleProject.getDependencies().addAll(
////                report.getDependencies().stream()
////                        .flatMap(entry -> entry.getValue().stream().map(dependency -> {
////                            String configuration = entry.getKey();
////                            return createGradleModuleDependency(configuration, dependency);
////                        })));
//
////        report.getDependencies()
////        this.collectDependencies(
////                report.getConfigurations(), repoId -> gradleBuild.repositories().get(repoId))
////                .forEach(gradleDep -> gradleBuild.dependencies().add(gradleDep));
//
//        report.getProject().getSubprojects().values().forEach(subProject -> {
//            convert(rootLocation, subProject.getPath(), reports, gradleProject);
//        });
//
//        return gradleProject;
//    }

//    private Collection<RemoteRepository> collectRepositories(List<RepositoryDto> repositories) {
//        return repositories.stream().map(RemoteRepository::from).collect(Collectors.toList());
//    }

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

//    private GradleModuleDependency createGradleModuleDependency(String configurationName, Dependency d) {
//
//        log.warn("TODO - simplified information - need to add artifact info etc .... !!!");
//
//        return GradleModuleDependency.from(configurationName,
//                GradleModuleIdentifier.from(d.getGroup(), d.getArtifact()),
//                GradleVersionSpecification.from(d.getVersion()),
//                module -> {
//                    // TODO what if a module defines multipe artifacts ?!
//                    // need to fix that in sentinel first ?
//                    module.addArtifact(artifact -> {
//                        // TODO most important properties missing ?
////                        artifact.setExtension();
////                        artifact.setName();
//                        artifact.setClassifier(d.getClassifier());
//                        artifact.setType(d.getType());
//                    });
//                });
//    }
}
