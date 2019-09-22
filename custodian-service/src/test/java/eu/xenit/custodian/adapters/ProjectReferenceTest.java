package eu.xenit.custodian.adapters;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.domain.repository.scm.ProjectReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

public class ProjectReferenceTest {

    private static String userHome() {
        return System.getProperty("user.home");
    }

    private static Path userHomePath() {
        return Paths.get(userHome());
    }

    @Test
    public void testUserHomeReference_fromPath() {
        ProjectReference projectRef = ProjectReference.from(userHomePath());
        assertThat(projectRef)
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("file://" + userHome() + "/");
    }

    @Test
    public void testUserHomeReference_fromStringWithoutScheme() {
        ProjectReference projectRef = ProjectReference.from(userHome());
        assertThat(projectRef)
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("file://" + userHome() + "/");
    }

    @Test
    public void testUserHomeReference_fromStringWithScheme() {
        ProjectReference projectRef = ProjectReference.from("file://" + userHome());
        assertThat(projectRef)
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("file://" + userHome());
    }

    @Test
    public void testGitHub_sshLink() {
        assertThat(ProjectReference.from("ssh://git@github.com:xenit-eu/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("ssh://git@github.com:xenit-eu/custodian.git");
    }


    @Test
    public void testGitHub_shortSshLink() {
        assertThat(ProjectReference.from("git@github.com:xenit-eu/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("ssh://git@github.com:xenit-eu/custodian.git");
    }

    @Test
    public void testGitHub_httpsLink() {
        assertThat(ProjectReference.from("https://github.com/xenit-eu/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("https://github.com/xenit-eu/custodian.git");
    }


    @Test
    public void testBitBucket_sshLink() {
        assertThat(ProjectReference.from("ssh://git@bitbucket.com:xenit/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("ssh://git@bitbucket.com:xenit/custodian.git");
    }


    @Test
    public void testBitBucket_shortReference() {
        assertThat(ProjectReference.from("git@bitbucket.org:xenit/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("ssh://git@bitbucket.org:xenit/custodian.git");
    }
}