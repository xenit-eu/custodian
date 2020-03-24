package eu.xenit.custodian.adapters.buildsystem.gradle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.SentinelGradleProjectAnalyzer;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.nio.file.Paths;
import org.junit.Test;

public class IntegrationTests {
    @Test
    public void inception() throws MetadataAnalyzerException {
        SentinelGradleProjectAnalyzer sentinel = new SentinelGradleProjectAnalyzer();
        ClonedRepositoryHandle handle = mock(ClonedRepositoryHandle.class);
        when(handle.getLocation()).thenReturn(Paths.get("../../.."));
//        when(handle.getReference()).thenReturn(ProjectReferenceParser.from("."));

        ClonedRepositorySourceMetadata project = mock(ClonedRepositorySourceMetadata.class);
        when(project.buildsystems()).thenReturn(new BuildSystemsContainer());

        sentinel.analyze(project, handle);

        assertThat(project)
                .isNotNull()
                .satisfies(p -> {
                    assertThat(p.buildsystems().get(GradleBuildSystem.ID))
                            .isNotNull();
//                            .satisfies(gradleBuild -> assertThat(gradleBuild.subprojects()).size()
                            //.satisfies(gradle -> assertThat(gradle.dependencies().stream()).contains()
                });

    }
}
