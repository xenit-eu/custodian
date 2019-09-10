package eu.xenit.custodian.domain.service.analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import eu.xenit.custodian.domain.model.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.repository.analysis.ProjectAnalyzer;
import org.junit.Test;

public class DefaultProjectAnalysisServiceTest {

    @Test
    public void analyze() {
        ProjectAnalyzer projectAnalyzer = mock(ProjectAnalyzer.class);
        ProjectHandle handle = mock(ProjectHandle.class);
        when(projectAnalyzer.analyze(any())).thenReturn(new ProjectMetadata(mock(ProjectReference.class)));


        ProjectMetadata metadata = new DefaultProjectAnalysisService(projectAnalyzer).analyze(handle);

        assertThat(metadata).isNotNull();
    }
}