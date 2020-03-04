package eu.xenit.custodian.domain.service.scm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;


import eu.xenit.custodian.adapters.service.scm.NullSourceControlHandler;
import eu.xenit.custodian.adapters.service.scm.StubbedSourceControlHandler;
import eu.xenit.custodian.domain.scm.CompositeSourceControlHandler;
import eu.xenit.custodian.domain.project.ProjectReferenceParser;
import eu.xenit.custodian.ports.api.ProjectHandle;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import eu.xenit.custodian.ports.spi.scm.UnsupportedProjectReference;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class DefaultSourceControlManagementServiceTest {

    @Test
    public void noHandlerThrowsException() {
        SourceControlHandler service = new CompositeSourceControlHandler(Collections.emptyList());

        assertThatExceptionOfType(UnsupportedProjectReference.class).isThrownBy(() -> {
            service.checkout(ProjectReferenceParser.from("ssh://git@github.com:xenit-eu/custodian.git"));
        }).withMessageContaining("No handler");
    }

    @Test
    public void checkoutWithFirstApplicableHandler() throws IOException {
        ProjectHandle handle = mock(ProjectHandle.class);

        SourceControlHandler service = new CompositeSourceControlHandler(
                Collections.singletonList(new StubbedSourceControlHandler(handle)));

        ProjectHandle checkout = service
                .checkout(ProjectReferenceParser.from("ssh://git@github.com:xenit-eu/custodian.git"));

        assertThat(checkout).isEqualTo(handle);
    }

    @Test
    public void shouldUseFirstApplicableHandler() throws IOException {
        ProjectHandle handle = mock(ProjectHandle.class);

        SourceControlHandler service = new CompositeSourceControlHandler(Arrays.asList(
                new NullSourceControlHandler(),
                new StubbedSourceControlHandler(handle),
                new NullSourceControlHandler()
        ));

        ProjectHandle checkout = service
                .checkout(ProjectReferenceParser.from("ssh://git@github.com:xenit-eu/custodian.git"));

        assertThat(checkout).isEqualTo(handle);
    }
}