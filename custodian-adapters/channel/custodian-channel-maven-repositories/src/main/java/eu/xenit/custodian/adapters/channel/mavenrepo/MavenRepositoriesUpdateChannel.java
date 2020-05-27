package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationDescriptor;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationProvider;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.ports.spi.buildsystem.Build;
import eu.xenit.custodian.domain.usecases.updates.LogicalChange;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.spi.buildsystem.Project;
import eu.xenit.custodian.ports.spi.updates.UpdateChannel;
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
    public Collection<LogicalChange> getChanges(ClonedRepositorySourceMetadata metadata) {

        List<LogicalChange> changeSetCollection = new ArrayList<>();
        Arrays.asList("gradle", "maven").forEach(buildSystemId -> {
            Build build = metadata.buildsystems().get(buildSystemId);
            changeSetCollection.addAll(this.getMavenRepoChangeSets(build));
        });

        return changeSetCollection;
    }

    Collection<LogicalChange> getMavenRepoChangeSets(Build build) {

        if (build == null) {
            return Collections.emptyList();
        }

        Project project = build.getRootProject();
        return project.getDependencies()
                        .stream()
                        .filter(MavenArtifactSpecificationProvider.class::isInstance)
                        .map(MavenArtifactSpecificationProvider.class::cast)
                        .map(MavenArtifactSpecificationProvider::getMavenArtifactsDescriptor)
                        .map(descriptor -> this.getDependencyUpdateChangeSet(build, project, descriptor))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }

    Optional<LogicalChange> getDependencyUpdateChangeSet(Build build,
            Project project, MavenArtifactSpecificationDescriptor artifact) {

        logger.info("Looking for updates on {} - current requested version {}", artifact.getModuleId(),
                artifact.getVersionSpec());

        // This update-channel only supports exact version specs, not version ranges etc
        // There should be another update-channel which is responsible to pin versions from the version-range-spec
        Optional<MavenModuleVersion> optionalPinnedVersion = artifact.getVersionSpec().asPinnedVersion();
        if (optionalPinnedVersion.isEmpty()) {
            return Optional.empty();
        }

        MavenArtifactDependencyVersionUpdate change = this.updateStrategy.options(artifact)
                .map(plan -> plan.execute(this.mavenArtifactResolver, artifact))
                .reduce(new MavenArtifactDependencyVersionUpdate(build, project, artifact.getDependency()),
                        MavenArtifactDependencyVersionUpdate::addProposal, (c1, c2) -> {
                            throw new UnsupportedOperationException();
                        });

        return Optional.of(change);

    }

}
