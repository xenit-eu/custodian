package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionRangeQueryResult;

public interface MavenArtifactResolver {

    MavenVersionRangeQueryResult resolve(MavenArtifactSpecification coordinates);
}
