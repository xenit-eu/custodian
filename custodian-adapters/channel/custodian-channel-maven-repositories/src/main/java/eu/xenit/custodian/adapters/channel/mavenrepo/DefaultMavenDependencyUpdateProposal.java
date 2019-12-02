package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionRangeQueryResult;
import eu.xenit.custodian.adapters.channel.mavenrepo.MavenDependencyUpdateStrategy.MavenDependencyUpdatePlan;
import eu.xenit.custodian.adapters.channel.mavenrepo.MavenDependencyUpdateStrategy.MavenDependencyUpdateProposal;

public class DefaultMavenDependencyUpdateProposal implements MavenDependencyUpdateProposal {

    private final MavenDependencyUpdatePlan plan;
    private final MavenVersionRangeQueryResult result;

    public DefaultMavenDependencyUpdateProposal(MavenDependencyUpdatePlan plan, MavenVersionRangeQueryResult result){

        this.plan = plan;
        this.result = result;
    }

    @Override
    public MavenDependencyUpdatePlan getPlan() {
        return this.plan;
    }

    @Override
    public MavenVersionRangeQueryResult getQueryResult() {
        return this.result;
    }

    @Override
    public String toString() {
        return this.getVersion().map(MavenModuleVersion::getValue).orElse("<none>")
                + " ("+this.getPlan().getName()+")";
    }
}
