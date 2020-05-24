package eu.xenit.custodian.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemException;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystemPort;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.io.IOException;
import org.junit.jupiter.api.Test;


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
    public void testFactoryCustomizers() throws IOException, BuildSystemException {
        SourceControlHandler scm = mock(SourceControlHandler.class);
        when(scm.canHandle(any())).thenReturn(true);
        when(scm.checkout(any())).thenReturn(mock(ClonedRepositoryHandle.class));

        BuildSystemPort buildSystemPort = mock(BuildSystemPort.class);

        CustodianFactory factory = CustodianFactory.create(settings -> {
            settings.withScmHandler(scm).withBuildSystem(buildSystemPort);
        });

        factory.create().checkoutProject(mock(SourceRepositoryReference.class));
        verify(scm).checkout(any());

        factory.create().analyzeProjectModel(mock(ClonedRepositoryHandle.class));
        verify(buildSystemPort).getBuild(any());
    }


}