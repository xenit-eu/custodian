package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import eu.xenit.custodian.adapters.channel.mavenrepo.MavenDependencyUpdateStrategy.MavenDependencyUpdateProposal;
import eu.xenit.custodian.ports.spi.build.Build;
import eu.xenit.custodian.asserts.build.buildsystem.ModuleDependency;
import eu.xenit.custodian.asserts.build.changes.DependencyUpdate;
import eu.xenit.custodian.asserts.build.changes.LogicalChangeBase;
import eu.xenit.custodian.ports.spi.build.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class MavenArtifactDependencyUpdate extends LogicalChangeBase implements DependencyUpdate {

    private final List<MavenDependencyUpdateProposal> proposals = new ArrayList<>();
    private final ModuleDependency dependency;

    MavenArtifactDependencyUpdate(Build build, Project project, ModuleDependency dependency) {
        super(build, project);

        this.dependency = Objects.requireNonNull(dependency, "Argument 'dependency' is required");
    }

    public MavenArtifactDependencyUpdate addProposal(MavenDependencyUpdateProposal proposal) {
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
        throw new RuntimeException("not yet implemented");
    }
}
