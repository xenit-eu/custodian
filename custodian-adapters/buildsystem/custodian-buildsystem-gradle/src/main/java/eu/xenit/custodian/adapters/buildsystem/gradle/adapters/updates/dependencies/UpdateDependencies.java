package eu.xenit.custodian.adapters.buildsystem.gradle.adapters.updates.dependencies;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleModuleDependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleProject;
import eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto.Dependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.spi.updates.GradleBuildUpdatePort;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateDependencies implements GradleBuildUpdatePort {

    private final Collection<DependencyUpdatePort> updates;

    public UpdateDependencies(Collection<DependencyUpdatePort> updates) {
        Objects.requireNonNull(updates, "Argument 'updates' is required");

        this.updates = updates;
    }

    @Override
    public Stream<BuildUpdateProposal> getUpdateProposals(GradleBuild build) {
        Objects.requireNonNull(build, "Argument 'build' is required");
        return build.getRootProject().getAllProjects()
                .stream()

                // update dependencies for this project
                // at this point do not align accross sub-projects
                .map(project -> this.getProjectDependencyUpdates(build, project))

                // should we score/order those proposals ?

                // flatten the stream of streams
                .flatMap(Function.identity());
    }

    Stream<BuildUpdateProposal> getProjectDependencyUpdates(GradleBuild build, GradleProject project) {
        return project.getDependencies().stream()
                .filter(GradleModuleDependency.class::isInstance)
                .map(GradleModuleDependency.class::cast)
                .map(dependency -> getDependencyUpdate(build, project, dependency))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<BuildUpdateProposal> getDependencyUpdate(GradleBuild build, GradleProject project,
            GradleModuleDependency dependency) {
        return this.updates.stream()
                .map(port -> port.apply(build, project, dependency))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
//    Stream<BuildUpdateProposal> getProjectDependencyUpdates(GradleProject project) {
//        return project.getDependencies().stream()
//                .filter(GradleModuleDependency.class::isInstance)
//                .map(GradleModuleDependency.class::cast)
//                .map(dependency -> this.getDependencyUpdateProposal(project, dependency))
//                .filter(Optional::isPresent)
//                .map(Optional::get);
//    }
//
//    Optional<BuildUpdateProposal> getDependencyUpdateProposal(
//            GradleProject project,
//            GradleModuleDependency dependency) {
//
//        log.info("Looking for dependency updates on {}:{}",
//                dependency.getModuleId(), dependency.getVersionSpec());
//
//        Optional<BuildUpdateProposal> dependencyUpdateProposal = this.updates.stream()
//                .map(update -> update.apply(this.build, project, dependency))
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .findFirst();
//
//
//
//        return dependencyUpdateProposal;
//
//    }

}
