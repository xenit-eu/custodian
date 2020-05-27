package eu.xenit.custodian.adapters.gradle.updates.impl.dependencies;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleProject;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;

public class DependencyVersionUpdate implements BuildUpdateProposal {

    private final GradleBuild build;
    private final GradleProject project;
    private final GradleModuleDependency dependency;
    private final ResolverArtifactVersion version;

    public DependencyVersionUpdate(GradleBuild build, GradleProject project, GradleModuleDependency dependency,
            ResolverArtifactVersion version)
    {
        this.build = build;
        this.project = project;
        this.dependency = dependency;
        this.version = version;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
