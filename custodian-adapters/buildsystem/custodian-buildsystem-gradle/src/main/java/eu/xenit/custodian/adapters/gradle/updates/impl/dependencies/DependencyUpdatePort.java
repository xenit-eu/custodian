package eu.xenit.custodian.adapters.gradle.updates.impl.dependencies;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleProject;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import java.util.Optional;

public interface DependencyUpdatePort {

    Optional<BuildUpdateProposal> apply(GradleBuild build,
            GradleProject project, GradleModuleDependency dependency);
}
