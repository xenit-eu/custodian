package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import org.junit.jupiter.api.Test;

class ResolverArtifactSpecificationTest {

    @Test
    void testFrom() {
        ResolverArtifactSpecification spec = ResolverArtifactSpecification
                .from("org.apache.httpcomponents", "httpclient", "4.5.12");

        assertThat(spec.getGroupId()).isEqualTo("org.apache.httpcomponents");
        assertThat(spec.getArtifactId()).isEqualTo("httpclient");
        assertThat(spec.getVersion()).isEqualTo("4.5.12");
        assertThat(spec.getClassifier()).isEqualTo("");
        assertThat(spec.getExtension()).isEqualTo("jar");
    }

    @Test
    void testFullFrom() {
        ResolverArtifactSpecification spec = ResolverArtifactSpecification
                .from("org.apache.httpcomponents", "httpclient", "4.5.12", "tests", "war");

        assertThat(spec.getGroupId()).isEqualTo("org.apache.httpcomponents");
        assertThat(spec.getArtifactId()).isEqualTo("httpclient");
        assertThat(spec.getVersion()).isEqualTo("4.5.12");
        assertThat(spec.getClassifier()).isEqualTo("tests");
        assertThat(spec.getExtension()).isEqualTo("war");
    }


    @Test
    void testCustomizer() {
        ResolverArtifactSpecification spec = ResolverArtifactSpecification
                .from("org.apache.httpcomponents", "httpclient", "4.5.12")
                .customize(artifact -> {
                    artifact.setClassifier("tests");
                    artifact.setExtension("war");
                });

        assertThat(spec.getGroupId()).isEqualTo("org.apache.httpcomponents");
        assertThat(spec.getArtifactId()).isEqualTo("httpclient");
        assertThat(spec.getVersion()).isEqualTo("4.5.12");
        assertThat(spec.getClassifier()).isEqualTo("tests");
        assertThat(spec.getExtension()).isEqualTo("war");
    }


}