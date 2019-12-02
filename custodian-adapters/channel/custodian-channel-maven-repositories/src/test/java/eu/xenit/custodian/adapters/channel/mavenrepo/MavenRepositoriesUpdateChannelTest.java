package eu.xenit.custodian.adapters.channel.mavenrepo;


import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleDependency;
import eu.xenit.custodian.domain.changes.ChangeSet;
import java.util.Optional;
import org.junit.Test;

public class MavenRepositoriesUpdateChannelTest {

    private MavenModuleDependency apacheHttpClientDependency = MavenModuleDependency
            .from("org.apache.httpcomponents", "httpclient", "4.3.5", "compile");

    @Test
    public void test() {

        // we should inject a mocked artifact resolver here !
        MavenRepositoriesUpdateChannel channel = new MavenRepositoriesUpdateChannel(new DefaultMavenArtifactResolver());

        Optional<ChangeSet> optionalChangeSet = channel.getDependencyUpdateChangeSet(
                apacheHttpClientDependency);

//
        assertThat(optionalChangeSet)
                .isPresent()
                .hasValueSatisfying(changeSet -> {
                    assertThat(changeSet)
                            .isNotNull()
                            .isInstanceOf(MavenArtifactDependencyUpdate.class);
                });

//        Assert.fail("commented out stuff below, didn't compile, WIP");

//        Collection<ChangeSet> changeSets = channel.getMavenRepoChangeSets(test);
//
//        assertThat(changeSets).isNotEmpty();
    }


}