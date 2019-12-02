package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.CompositeMavenVersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationDescriptor;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationProvider;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import eu.xenit.custodian.domain.buildsystem.Build;
import eu.xenit.custodian.domain.buildsystem.Dependency;
import eu.xenit.custodian.domain.changes.ChangeSet;
import eu.xenit.custodian.domain.metadata.ProjectMetadata;
import eu.xenit.custodian.ports.spi.channel.UpdateChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenRepositoriesUpdateChannel implements UpdateChannel {

    private static final Logger logger = LoggerFactory.getLogger(MavenRepositoriesUpdateChannel.class);

    private final MavenArtifactResolver mavenArtifactResolver;
    private final MavenDependencyUpdateStrategy updateStrategy;

    public MavenRepositoriesUpdateChannel(MavenArtifactResolver mavenArtifactResolver) {
        this(mavenArtifactResolver, new DefaultMavenUpdateStrategy());
    }

    public MavenRepositoriesUpdateChannel(MavenArtifactResolver mavenArtifactResolver,
            MavenDependencyUpdateStrategy updateStrategy) {
        this.mavenArtifactResolver = mavenArtifactResolver;
        this.updateStrategy = updateStrategy;
    }

    @Override
    public Collection<ChangeSet> getChangeSets(ProjectMetadata metadata) {

        List<ChangeSet> changeSetCollection = new ArrayList<>();
        Arrays.asList("gradle", "maven").forEach(buildSystemId -> {
            Build build = metadata.buildsystems().get(buildSystemId);
            changeSetCollection.addAll(this.getMavenRepoChangeSets(build));
        });

        return changeSetCollection;
    }

    Collection<ChangeSet> getMavenRepoChangeSets(Build<Build, Dependency> build) {

        if (build == null) {
            return Collections.emptyList();
        }

        Collection<ChangeSet> result =
                build.dependencies()
                        .stream()
                        .filter(MavenArtifactSpecificationProvider.class::isInstance)
                        .map(MavenArtifactSpecificationProvider.class::cast)
                        .map(MavenArtifactSpecificationProvider::getMavenArtifactsDescriptor)
                        .map(this::getDependencyUpdateChangeSet)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());

        return result;
    }

    Optional<ChangeSet> getDependencyUpdateChangeSet(MavenArtifactSpecificationDescriptor artifact) {

        logger.info("Looking for updates on {} - current requested version {}", artifact.getModuleId(),
                artifact.getVersionSpec());

        // This update-channel only supports exact version specs, not version ranges etc
        // There should be another update-channel which is responsible to pin versions from the version-range-spec
        Optional<MavenModuleVersion> optionalPinnedVersion = artifact.getVersionSpec().asPinnedVersion();
        if (!optionalPinnedVersion.isPresent()) {
            return Optional.empty();
        }

        MavenArtifactDependencyUpdate change = this.updateStrategy.options(artifact)
                .map(plan -> plan.execute(this.mavenArtifactResolver, artifact))
                .reduce(new MavenArtifactDependencyUpdate(artifact.getDependency()), MavenArtifactDependencyUpdate::addProposal, (c1, c2) -> {
                    throw new UnsupportedOperationException();
                });

        return Optional.of(change);

    }

    private static MavenVersionRangeQueryResult getDependencyUpdate(MavenArtifactResolver resolver,
            MavenArtifactSpecificationDescriptor dependency,
            MavenVersionSpecification versionSpec) {

        // If a module dependency has multiple artifacts configured (gradle only, not supported by maven pom.xml),
        // we should get the latest version that both artifacts have in common
//        Stream<MavenVersionRangeQueryResult> mavenVersionRangeQueryResultStream =
        List<MavenVersionRangeQueryResult> collect = dependency
                .getArtifactSpecs()
                .stream()
                .map(spec -> spec.customize(config -> {
                    config.setVersionSpec(versionSpec);
                }))
                .map(resolver::resolve)
                .peek(result -> {
                    logger.info("resolved {} '{}' to {}", dependency.getModuleId().getId(), versionSpec,
                            result.getHighestVersion());
                })
                .collect(Collectors.toList());

        if (collect.size() == 1) {
            return collect.get(0);
        }

        return new CompositeMavenVersionRangeQueryResult(versionSpec, collect);
    }

//    boolean isPinnedVersionSpec(MavenVersionSpecification versionSpec) {
//        // Relying on DefaultArtifactVersion parsing to find out this is a pinned version or not
//        DefaultArtifactVersion version = new DefaultArtifactVersion(versionSpec.toString());
//
//        if (versionSpec.toString().contentEquals(version.getQualifier())) {
//            return false;
//        }
//
//        return true;
//    }


}
