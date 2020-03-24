package eu.xenit.custodian.adapters.buildsystem.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.domain.buildsystem.GroupArtifactModuleIdentifier;
import java.util.Collections;
import org.junit.Test;

public class DefaultGradleModuleDependencyTest {

    public static final String IMPLEMENTATION_CONFIGURATION_NAME = "implementation";

    @Test
    public void testMavenArtifacts() {
        GradleModuleDependency junit = new DefaultGradleModuleDependency(
                IMPLEMENTATION_CONFIGURATION_NAME,
                GroupArtifactModuleIdentifier.from("junit", "junit"),
                GradleVersionSpecification.from("4.12"),
                Collections.emptySet());

        assertThat(junit.getMavenArtifactsDescriptor())
                .satisfies(desc -> {
                    assertThat(desc.getModuleId().getGroup()).isEqualTo("junit");
                    assertThat(desc.getModuleId().getName()).isEqualTo("junit");
                    assertThat(desc.getVersionSpec().getValue()).isEqualTo("4.12");

                    // there are no artifact specs declared, it's all implicit ?
                    assertThat(desc.getArtifactSpecs()).isEmpty();
                });

    }
}