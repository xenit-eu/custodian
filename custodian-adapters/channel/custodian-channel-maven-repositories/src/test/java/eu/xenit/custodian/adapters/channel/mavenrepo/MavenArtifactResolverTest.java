package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionRangeQueryResult;
import org.junit.Test;

public class MavenArtifactResolverTest {

    MavenArtifactSpecification JUNIT = MavenArtifactSpecification.from("junit", "junit", "4.12");
    MavenArtifactSpecification APACHE_HTTPCLIENT = MavenArtifactSpecification
            .from("org.apache.httpcomponents", "httpclient", "4.4");

    // TODO this is not a unit tests, this actually queries maven central!
    @Test
    public void resolve() {
        DefaultMavenArtifactResolver resolver = new DefaultMavenArtifactResolver();

        resolver.resolve(JUNIT);
        resolver.resolve(APACHE_HTTPCLIENT);
    }
}