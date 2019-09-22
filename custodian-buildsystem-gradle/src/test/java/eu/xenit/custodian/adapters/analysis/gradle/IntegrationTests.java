package eu.xenit.custodian.adapters.analysis.gradle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.domain.repository.scm.ProjectHandle;
import eu.xenit.custodian.domain.repository.scm.ProjectReference;
import eu.xenit.custodian.domain.repository.analysis.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.repository.analysis.AnalyzerException;
import java.nio.file.Paths;
import org.junit.Test;

public class IntegrationTests {
    @Test
    public void inception() throws AnalyzerException {
        SentinelGradleProjectAnalyzer sentinel = new SentinelGradleProjectAnalyzer();
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
