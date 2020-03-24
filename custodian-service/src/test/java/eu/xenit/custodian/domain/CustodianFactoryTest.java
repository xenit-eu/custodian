package eu.xenit.custodian.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import eu.xenit.custodian.ports.spi.metadata.ProjectMetadataAnalyzer;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.io.IOException;
import org.junit.Test;


public class CustodianFactoryTest {

//    @Test
//    public void testFactoryDefaultSettings() {
//        CustodianFactory factory = CustodianFactory.withDefaultSettings();
//
//        CustodianFactoryAssert.assertThat(factory)
//                .hasSourceControlHandler(LocalFolderSourceControlHandler.class)
//
//                // no metadata analyzers on this classpath
//                .hasNoMetadataAnalyzers();
//    }

    @Test
    public void testFactoryCustomizers() throws IOException, MetadataAnalyzerException {
        SourceControlHandler scm = mock(SourceControlHandler.class);
        when(scm.canHandle(any())).thenReturn(true);

        ProjectMetadataAnalyzer metadata = mock(ProjectMetadataAnalyzer.class);

        CustodianFactory factory = CustodianFactory.create(settings -> {
            settings.withScmHandler(scm).withProjectMedataAnalyzer(metadata);
        });

        factory.create().checkoutProject(mock(SourceRepositoryReference.class));
        verify(scm).checkout(any());

        factory.create().extractMetadata(mock(ClonedRepositoryHandle.class));
        verify(metadata).analyze(any(), any());
    }


}