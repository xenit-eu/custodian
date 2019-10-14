package eu.xenit.custodian.adapters.metadata.gradle;

import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleModuleDependency;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleModuleDependency.Builder;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleRepository;
import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.RepositoryFactory;
import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.Configuration;
import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.Dependency;
import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.Report;
import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.RepositoryDto;
import eu.xenit.custodian.domain.metadata.buildsystem.Repository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        gradleBuild.dependencies().addAll(this.collectDependencies(
                report.getConfigurations(), repoId -> gradleBuild.repositories().get(repoId)));


        report.getProject().getSubprojects().values().forEach(subProject -> {
            convert(subProject.getPath(), reports, gradleBuild);
        });

        return gradleBuild;
    }

    private Collection<GradleRepository> collectRepositories(List<RepositoryDto> repositories) {
        RepositoryFactory factory = new RepositoryFactory();
        return repositories.stream().map(factory::from).collect(Collectors.toList());
    }

    private List<GradleModuleDependency> collectDependencies(
            Map<String, Configuration> configurationMap,
            Function<String, GradleRepository> repoLookup) {
        return configurationMap.entrySet()
                .stream()
                .flatMap(entry -> {
                    String configurationName = entry.getKey();
                    Configuration configuration = entry.getValue();
                    return configuration.getDependencies()
                            .stream()
                            .map(d -> createGradleDependency(repoLookup, configurationName, d));
                })
                .collect(Collectors.groupingBy(GradleModuleDependency::getId))
                .values()
                .stream()
                .map(dependenciesById -> dependenciesById.stream()
                        .reduce((dependency, dependency2) -> {
                            Builder builder = dependency.toBuilder();
                            dependency2.getConfigurations().forEach(builder::configuration);

                            return builder.build();
                        })
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(dep -> {
                    String msg = String.format("> %s - configurations: %s", dep.getId(), dep.getConfigurations());
                    System.out.println(msg);
                })
                .collect(Collectors.toList());

    }

    private GradleModuleDependency createGradleDependency(Function<String, GradleRepository> repoLookup,
            String configurationName, Dependency d) {
        Builder builder = GradleModuleDependency.from(d)
                .configuration(configurationName);

        String repositoryId = d.getResolution() != null ? d.getResolution().getRepository() : null;
        Repository repository = repoLookup.apply(repositoryId);
        if (repository != null) {
            builder.repository(repository);
        }

        return builder.build();
    }


}
