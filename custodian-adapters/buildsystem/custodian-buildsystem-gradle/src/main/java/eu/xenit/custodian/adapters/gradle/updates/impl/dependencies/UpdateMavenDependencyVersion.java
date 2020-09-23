package eu.xenit.custodian.adapters.gradle.updates.impl.dependencies;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenResolverApi;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenVersionRange;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverGroupArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.adapters.gradle.updates.usecases.ports.GradleBuildUpdateProposal;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpdateMavenDependencyVersion implements GradleDependencyUpdatePort {

    private final MavenResolverApi mavenResolver;

    public UpdateMavenDependencyVersion(MavenResolverApi mavenResolver) {
        this.mavenResolver = mavenResolver;
    }

    @Override
    public Optional<GradleBuildUpdateProposal> apply(GradleBuild build,
            GradleProject project, GradleModuleDependency dependency) {

        // convert gradle-artifact-specs to maven-resolver-artifact-specs
        var coords = ResolverGroupArtifact.from(dependency.getGroup(), dependency.getName());

        var version = ResolverVersionSpecification.from(dependency.getVersionSpec().getValue());
        Optional<ResolverArtifactVersion> optionalPinnedVersion = version.asPinnedVersion();
        if (optionalPinnedVersion.isEmpty()) {
            // the version is not pinned (e.g. it's a range)
            // another update-adaptor should be concerned with pinning versions
            return Optional.empty();
        }

        var artifacts = dependency.getArtifacts().stream()
                .map(artifact -> ResolverArtifact.from(artifact.getClassifier(), artifact.getExtension()))
                .collect(Collectors.toSet());

        var repositories = project.getRepositories().items()
                .map(repo -> ResolverMavenRepository.from(repo.getId(), repo.getUrl()))
                .collect(Collectors.toList());

        // many update strategies are possible
        // 1. update to latest minor
        // 2. update first to latest incremental (with auto-merge?), next iteration update to latest minor
        // 3. major ?

        // those strategies could be extracted in separate objects in the future
        // (once we do, is it possible to make those strategies independent from maven/ivy/... ?)
        // the result could be wrapped in an object and passed to DependencyVersionUpdate ?

        // for now, we hardcode (1) - update to latest minor
        var minorVersionRange = MavenVersionRange.getMinorVersionRange(optionalPinnedVersion.get());
        var versionRangeQueryResult = this.mavenResolver
                .resolveVersionRange(coords, minorVersionRange, artifacts, repositories);

        return versionRangeQueryResult.getHighestVersion()
                .map(newVersion -> new DependencyVersionUpdateProposal(build, project, dependency, newVersion));

    }
}
