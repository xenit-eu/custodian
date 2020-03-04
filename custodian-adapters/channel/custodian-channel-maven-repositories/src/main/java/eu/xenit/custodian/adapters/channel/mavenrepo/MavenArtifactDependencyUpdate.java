package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import eu.xenit.custodian.adapters.channel.mavenrepo.MavenDependencyUpdateStrategy.MavenDependencyUpdateProposal;
import eu.xenit.custodian.domain.buildsystem.Build;
import eu.xenit.custodian.domain.buildsystem.ModuleDependency;
import eu.xenit.custodian.domain.changes.DependencyUpdate;
import eu.xenit.custodian.domain.changes.LogicalChangeBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MavenArtifactDependencyUpdate extends LogicalChangeBase implements DependencyUpdate {

    private final List<MavenDependencyUpdateProposal> proposals = new ArrayList<>();
    private final ModuleDependency dependency;

    MavenArtifactDependencyUpdate(Build build, ModuleDependency dependency) {
        super(build);

        Objects.requireNonNull(build, "Argument 'build' is required");
        Objects.requireNonNull(dependency, "Argument 'dependency' is required");

        this.dependency = dependency;
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
        throw new NotImplementedException();
    }
}
