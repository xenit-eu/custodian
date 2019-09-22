package eu.xenit.custodian.adapters.analysis.gradle;

import eu.xenit.custodian.adapters.analysis.gradle.build.GradleBuild;
import eu.xenit.custodian.adapters.analysis.gradle.build.GradleModuleDependency;
import eu.xenit.custodian.adapters.analysis.gradle.build.GradleModuleDependency.Builder;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Configuration;
import eu.xenit.custodian.adapters.analysis.gradle.sentinel.Report;
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
        gradleBuild.dependencies().addAll(this.collectDependencies(report.getConfigurations()));

        report.getProject().getSubprojects().values().forEach(subProject -> {
            convert(subProject.getPath(), reports, gradleBuild);
        });

        return gradleBuild;
    }

    private List<GradleModuleDependency> collectDependencies(Map<String, Configuration> configurationMap) {
        return configurationMap.entrySet()
                .stream()
                .flatMap(entry -> {
                    String configurationName = entry.getKey();
                    Configuration configuration = entry.getValue();
                    return configuration.getDependencies()
                            .stream()
                            .map(d -> GradleModuleDependency.from(d).configuration(configurationName).build());
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



}
