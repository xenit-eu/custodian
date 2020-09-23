package eu.xenit.custodian.adapters.gradle.updates.impl.dependencies;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdateProposal;
import eu.xenit.custodian.ports.spi.buildsystem.BuildModification;
import lombok.Getter;

public class DependencyVersionUpdateProposal implements GradleBuildUpdateProposal {

    private final GradleBuild build;
    private final GradleProject project;

    @Getter
    private final GradleModuleDependency dependency;

    @Getter
    private final ResolverArtifactVersion version;

    public DependencyVersionUpdateProposal(GradleBuild build, GradleProject project, GradleModuleDependency dependency,
            ResolverArtifactVersion version) {
        this.build = build;
        this.project = project;
        this.dependency = dependency;
        this.version = version;
    }

    @Override
    public String getDescription() {
        return String.format("Update %s from %s to %s",
                dependency.getName(), dependency.getVersionSpec().getValue(), version.getValue());
    }

    @Override
    public BuildModification apply() {
        return this.build.modify().updateDependency(this.project, this.dependency, dep -> {
            dep.setVersion(this.version.getValue());
        });
    }
}
