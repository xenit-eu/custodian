package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecificationDescriptor;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import java.util.Optional;
import java.util.stream.Stream;

public interface MavenDependencyUpdateStrategy {

    Stream<MavenDependencyUpdatePlan> options(MavenArtifactSpecificationDescriptor dependency);

    interface MavenDependencyUpdatePlan {
        String getName();

        MavenVersionSpecification getVersionSpecification(MavenModuleVersion currentVersion);
        MavenDependencyUpdateProposal execute(MavenArtifactResolver resolver, MavenArtifactSpecificationDescriptor dependency);
    }

    interface MavenDependencyUpdateProposal {
        MavenDependencyUpdatePlan getPlan();
        MavenVersionRangeQueryResult getQueryResult();

        default Optional<MavenModuleVersion> getVersion() {
            return this.getQueryResult().getHighestVersion();
        }

        default Optional<MavenVersionSpecification> getVersionSpec() {
            return this.getVersion().map(MavenVersionSpecification::from);
        }
    }
}
