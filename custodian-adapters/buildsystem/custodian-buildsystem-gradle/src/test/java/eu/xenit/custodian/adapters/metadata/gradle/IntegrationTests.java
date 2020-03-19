package eu.xenit.custodian.adapters.metadata.gradle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.adapters.metadata.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.asserts.build.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.ports.api.SourceRepositoryHandle;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.nio.file.Paths;
import org.junit.Test;

public class IntegrationTests {
    @Test
    public void inception() throws MetadataAnalyzerException {
        SentinelGradleProjectAnalyzer sentinel = new SentinelGradleProjectAnalyzer();
        SourceRepositoryHandle handle = mock(SourceRepositoryHandle.class);
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
