package eu.xenit.custodian.domain.service.scm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import eu.xenit.custodian.adapters.repository.scm.NullSourceControlHandler;
import eu.xenit.custodian.adapters.repository.scm.StubbedSourceControlHandler;
import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import eu.xenit.custodian.domain.model.exceptions.UnsupportedProjectReference;
import eu.xenit.custodian.domain.repository.scm.SourceControlHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class DefaultSourceControlManagementServiceTest {

    @Test
    public void noHandlerThrowsException() {
        SourceControlManagementService service = new DefaultSourceControlManagementService(Collections.emptyList());

        assertThatExceptionOfType(UnsupportedProjectReference.class).isThrownBy(() -> {
            service.checkout(ProjectReference.from("ssh://git@github.com:xenit-eu/custodian.git"));
        }).withMessageContaining("No handler");
    }

    @Test
    public void checkoutWithFirstApplicableHandler() throws IOException {
        ProjectHandle handle = mock(ProjectHandle.class);

        SourceControlManagementService service = new DefaultSourceControlManagementService(
                Collections.singletonList(new StubbedSourceControlHandler(handle)));

        ProjectHandle checkout = service
                .checkout(ProjectReference.from("ssh://git@github.com:xenit-eu/custodian.git"));

        assertThat(checkout).isEqualTo(handle);
    }

    @Test
    public void shouldUseFirstApplicableHandler() throws IOException {
        ProjectHandle handle = mock(ProjectHandle.class);

        SourceControlManagementService service = new DefaultSourceControlManagementService(Arrays.asList(
                new NullSourceControlHandler(),
                new StubbedSourceControlHandler(handle),
                new NullSourceControlHandler()
        ));

        ProjectHandle checkout = service
                .checkout(ProjectReference.from("ssh://git@github.com:xenit-eu/custodian.git"));

        assertThat(checkout).isEqualTo(handle);
    }
}