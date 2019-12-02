package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.channel.mavenrepo.MavenDependencyUpdateStrategy.MavenDependencyUpdateProposal;
import eu.xenit.custodian.domain.buildsystem.ModuleDependency;
import eu.xenit.custodian.domain.changes.ChangeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class MavenArtifactDependencyUpdate implements ChangeSet {

    private final List<MavenDependencyUpdateProposal> proposals = new ArrayList<>();
    private final ModuleDependency dependency;

    public MavenArtifactDependencyUpdate(ModuleDependency dependency) {

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
    public String toString() {
        return this.getClass().getSimpleName()
                + " - " + this.dependency
                + " -> " + proposal().map(Object::toString).orElse("<none>");
    }
}
