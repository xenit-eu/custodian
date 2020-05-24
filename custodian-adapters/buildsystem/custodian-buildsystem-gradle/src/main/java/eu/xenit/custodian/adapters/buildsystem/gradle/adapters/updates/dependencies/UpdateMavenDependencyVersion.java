package eu.xenit.custodian.adapters.buildsystem.gradle.adapters.updates.dependencies;

import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleModuleDependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleProject;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenResolverApi;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenVersionRange;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverGroupArtifact;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverVersionSpecification;
import eu.xenit.custodian.ports.spi.buildsystem.BuildUpdateProposal;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpdateMavenDependencyVersion implements DependencyUpdatePort {

    private final MavenResolverApi mavenResolver;

    public UpdateMavenDependencyVersion(MavenResolverApi mavenResolver) {

        this.mavenResolver = mavenResolver;
    }

    @Override
    public Optional<BuildUpdateProposal> apply(GradleBuild build,
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

        var repositories = project.getRepositories().builds()
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
                .map(newVersion -> {
                    return new DependencyVersionUpdate(build, project, dependency, newVersion);
                });

//
//        versionRangeQueryResult.getHighestVersion()
//
//        // when dependency.getArtifacts is empty, default classifier/extension is fine
//        MavenArtifactDependencyVersionUpdate change = this.updateStrategyFactory.options(artifact)
//                .map(plan -> plan.execute(this.mavenArtifactResolver, artifact))
//                .reduce(new MavenArtifactDependencyVersionUpdate(build, project, artifact.getDependency()),
//                        MavenArtifactDependencyVersionUpdate::addProposal, (c1, c2) -> {
//                            throw new UnsupportedOperationException();
//                        });
    }
}
