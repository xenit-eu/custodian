package eu.xenit.custodian.adapters.buildsystem.maven;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.Test;

public class DefaultMavenArtifactSpecificationTest {

    private final MavenModuleIdentifier MODULE = MavenModuleIdentifier.from("org.apache.httpcomponents", "httpclient");
    private final MavenVersionSpecification VERSION = MavenVersionSpecification.from("4.5.1");

    private final MavenArtifactSpecification DEFAULT = new DefaultMavenArtifactSpecification(
            MODULE, VERSION, null, null, null);

    @Test
    public void constructor_testRequiredArgs() {
        assertThatNullPointerException().isThrownBy(() -> {
            new DefaultMavenArtifactSpecification(null, VERSION, null, null, null);
        });
        assertThatNullPointerException().isThrownBy(() -> {
            new DefaultMavenArtifactSpecification(MODULE, null, null, null, null);
        });
    }

    @Test
    public void constructor_typeArgumentNotSupported() {
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> {
            new DefaultMavenArtifactSpecification(MODULE, VERSION, null, null, "test-jar");
        });
    }

    @Test
    public void default_toString() {

        assertThat(DEFAULT.toString())
                .isEqualTo("maven://org.apache.httpcomponents:httpclient:4.5.1");
    }

    @Test
    public void classifier_toString() {
        assertThat(DEFAULT.customize(c -> c.setClassifier("javadoc")).toString())
                .isEqualTo("maven://org.apache.httpcomponents:httpclient::javadoc:4.5.1");
    }

    @Test
    public void extension_toString() {
        assertThat(DEFAULT.customize(c -> c.setExtension("amp")).toString())
                .isEqualTo("maven://org.apache.httpcomponents:httpclient:amp:4.5.1");
    }

}