package eu.xenit.custodian.adapters.buildsystem.gradle.adapters.updates.dependencies;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleModuleDependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleProject;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import java.util.Optional;

public interface DependencyUpdatePort {

    Optional<BuildUpdateProposal> apply(GradleBuild build,
            GradleProject project, GradleModuleDependency dependency);
}
