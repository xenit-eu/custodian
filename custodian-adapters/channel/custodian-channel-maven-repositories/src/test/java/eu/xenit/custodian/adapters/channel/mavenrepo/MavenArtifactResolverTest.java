package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecification;
import org.junit.Test;

public class MavenArtifactResolverTest {

    MavenArtifactSpecification JUNIT = MavenArtifactSpecification.from("junit", "junit", "4.12");
    MavenArtifactSpecification APACHE_HTTPCLIENT = MavenArtifactSpecification
            .from("org.apache.httpcomponents", "httpclient", "4.4");

    @Test
    public void resolve() {
        DefaultMavenArtifactResolver resolver = new DefaultMavenArtifactResolver();

        resolver.resolve(JUNIT);
        resolver.resolve(APACHE_HTTPCLIENT);
    }
}