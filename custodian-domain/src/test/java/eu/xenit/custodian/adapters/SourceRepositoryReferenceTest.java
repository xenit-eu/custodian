package eu.xenit.custodian.adapters;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.domain.project.ProjectReferenceParser;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class SourceRepositoryReferenceTest {

    private static String userHome() {
        return System.getProperty("user.home");
    }

    private static Path userHomePath() {
        return Paths.get(userHome());
    }

    @Test
    public void testUserHomeReference_fromPath() {
        SourceRepositoryReference projectRef = ProjectReferenceParser.from(userHomePath());
        assertThat(projectRef)
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("file://" + userHome() + "/");
    }

    @Test
    public void testUserHomeReference_fromStringWithoutScheme() {
        SourceRepositoryReference projectRef = ProjectReferenceParser.from(userHome());
        assertThat(projectRef)
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("file://" + userHome() + "/");
    }

    @Test
    public void testUserHomeReference_fromStringWithScheme() {
        SourceRepositoryReference projectRef = ProjectReferenceParser.from("file://" + userHome());
        assertThat(projectRef)
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("file://" + userHome());
    }

    @Test
    public void testGitHub_sshLink() {
        assertThat(ProjectReferenceParser.from("ssh://git@github.com:xenit-eu/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("ssh://git@github.com:xenit-eu/custodian.git");
    }


    @Test
    public void testGitHub_shortSshLink() {
        assertThat(ProjectReferenceParser.from("git@github.com:xenit-eu/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("ssh://git@github.com:xenit-eu/custodian.git");
    }

    @Test
    public void testGitHub_httpsLink() {
        assertThat(ProjectReferenceParser.from("https://github.com/xenit-eu/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("https://github.com/xenit-eu/custodian.git");
    }


    @Test
    public void testBitBucket_sshLink() {
        assertThat(ProjectReferenceParser.from("ssh://git@bitbucket.com:xenit/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("ssh://git@bitbucket.com:xenit/custodian.git");
    }


    @Test
    public void testBitBucket_shortReference() {
        assertThat(ProjectReferenceParser.from("git@bitbucket.org:xenit/custodian.git"))
                .extracting(ref -> ref.getUri().toString())
                .isEqualTo("ssh://git@bitbucket.org:xenit/custodian.git");
    }
}