package eu.xenit.custodian.adapters.repository.analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.adapters.model.buildsystem.gradle.GradleBuildSystem;
import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import eu.xenit.custodian.domain.model.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.repository.analysis.AnalyzerException;
import java.nio.file.Paths;
import org.junit.Test;

public class SentinelGradlePojectAnalyzerTest {

    @Test
    public void inception() throws AnalyzerException {
        SentinelGradlePojectAnalyzer sentinel = new SentinelGradlePojectAnalyzer();
        ProjectHandle handle = mock(ProjectHandle.class);
        when(handle.getLocation()).thenReturn(Paths.get(".."));
        when(handle.getReference()).thenReturn(ProjectReference.from("."));

        ProjectMetadata project = sentinel.analyze(handle);

        assertThat(project)
                .isNotNull()
                .satisfies(p -> {
//                    assertThat(p.buildsystems().get(GradleBuildSystem.ID)).isNotNull();
                });

    }
}