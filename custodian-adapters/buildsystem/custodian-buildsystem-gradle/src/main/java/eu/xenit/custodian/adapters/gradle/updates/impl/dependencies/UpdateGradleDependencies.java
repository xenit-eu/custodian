package eu.xenit.custodian.adapters.gradle.updates.impl.dependencies;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdatePort;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdateProposal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateGradleDependencies implements GradleBuildUpdatePort {

    private final Collection<GradleDependencyUpdatePort> updates;
    
    public UpdateGradleDependencies(GradleDependencyUpdatePort... updates) {
        this(List.of(updates));
    }

    public UpdateGradleDependencies(Collection<GradleDependencyUpdatePort> updates) {
        Objects.requireNonNull(updates, "Argument 'updates' is required");

        this.updates = updates;
    }

    @Override
    public Stream<GradleBuildUpdateProposal> getUpdateProposals(GradleBuild build) {
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

    Stream<GradleBuildUpdateProposal> getProjectDependencyUpdates(GradleBuild build, GradleProject project) {
        return project.getDependencies().stream()
                .filter(GradleModuleDependency.class::isInstance)
                .map(GradleModuleDependency.class::cast)
                .map(dependency -> getDependencyUpdate(build, project, dependency))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<GradleBuildUpdateProposal> getDependencyUpdate(GradleBuild build, GradleProject project,
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
