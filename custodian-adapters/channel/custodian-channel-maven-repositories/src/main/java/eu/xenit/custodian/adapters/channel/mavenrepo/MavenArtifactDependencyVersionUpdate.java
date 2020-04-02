package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import eu.xenit.custodian.adapters.channel.mavenrepo.MavenDependencyUpdateStrategy.MavenDependencyUpdateProposal;
import eu.xenit.custodian.domain.changes.BuildModificationResult;
import eu.xenit.custodian.domain.changes.ChangeApplicationResult;
import eu.xenit.custodian.domain.changes.NoopChangeApplicationResult;
import eu.xenit.custodian.ports.spi.build.Build;
import eu.xenit.custodian.domain.buildsystem.ModuleDependency;
import eu.xenit.custodian.domain.changes.DependencyVersionUpdate;
import eu.xenit.custodian.domain.changes.LogicalChangeBase;
import eu.xenit.custodian.ports.spi.build.BuildModification;
import eu.xenit.custodian.ports.spi.build.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class MavenArtifactDependencyVersionUpdate extends LogicalChangeBase implements DependencyVersionUpdate {

    private final List<MavenDependencyUpdateProposal> proposals = new ArrayList<>();
    private final ModuleDependency dependency;

    MavenArtifactDependencyVersionUpdate(Build build, Project project, ModuleDependency dependency) {
        super(build, project);

        this.dependency = Objects.requireNonNull(dependency, "Argument 'dependency' is required");
    }

    public MavenArtifactDependencyVersionUpdate addProposal(MavenDependencyUpdateProposal proposal) {
        Objects.requireNonNull(proposal, "Argument 'variant' cannot be null");
        this.proposals.add(proposal);
        return this;
    }

    public Optional<MavenDependencyUpdateProposal> proposal() {
        return this.proposals.stream().findFirst();
    }

    public Stream<MavenDependencyUpdateProposal> variants() {
        return this.proposals.stream().skip(1);
    }

    @Override
    public ModuleDependency getDependency() {
        return this.dependency;
    }

    @Override
    public Optional<MavenVersionSpecification> getProposedVersion() {
        return this.proposal().flatMap(MavenDependencyUpdateProposal::getVersionSpec);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
                + " - " + this.dependency
                + " -> " + proposal().map(Object::toString).orElse("<none>");
    }


    @Override
    public ChangeApplicationResult apply() {

        Optional<MavenVersionSpecification> proposedVersion = this.getProposedVersion();

        if (!proposedVersion.isPresent()) {
            return NoopChangeApplicationResult.INSTANCE;
        }

        BuildModification result = this.getBuild().modify()
                .updateDependency(this.getProject(), this.getDependency(), dependency -> {
                    dependency.setVersion(proposedVersion.get().getValue());
                });

        return new BuildModificationResult(result, "Update ");
    }
}
