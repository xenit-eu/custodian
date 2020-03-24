package eu.xenit.custodian.adapters.channel.mavenrepo;


import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleDependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.DefaultGradleProject;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleBuild;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleModuleDependency;
import eu.xenit.custodian.adapters.buildsystem.gradle.GradleProject;
import eu.xenit.custodian.domain.changes.LogicalChange;
import eu.xenit.custodian.ports.spi.build.Build;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.Test;

public class MavenRepositoriesUpdateChannelTest {

    private MavenModuleDependency DEPENDENCY_APACHE_HTTPCLIENT_MAVEN = MavenModuleDependency
            .from("org.apache.httpcomponents", "httpclient", "4.3.5");

    private GradleModuleDependency DEPENDENCY_APACHE_HTTPCLIENT_GRADLE = GradleModuleDependency
            .from("implementation", "org.apache.httpcomponents", "httpclient", "4.3.5");

    @Test
    public void test() {

        // TODO we should inject a mocked artifact resolver here !
        MavenRepositoriesUpdateChannel channel = new MavenRepositoriesUpdateChannel(new DefaultMavenArtifactResolver());

        GradleProject project = new DefaultGradleProject("test");
        project.getDependencies().add(DEPENDENCY_APACHE_HTTPCLIENT_GRADLE);
        Build build = new GradleBuild(Paths.get(""), project);

        Optional<LogicalChange> optionalChangeSet = channel.getDependencyUpdateChangeSet(build,
                project, DEPENDENCY_APACHE_HTTPCLIENT_MAVEN);

//
        assertThat(optionalChangeSet)
                .isPresent()
                .hasValueSatisfying(changeSet -> {
                    assertThat(changeSet)
                            .isNotNull()
                            .isInstanceOf(MavenArtifactDependencyVersionUpdate.class);

                });
    }
}