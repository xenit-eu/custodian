package eu.xenit.custodian.domain.service.scm;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import eu.xenit.custodian.domain.model.exceptions.UnsupportedProjectReference;
import eu.xenit.custodian.domain.repository.scm.SourceControlHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultSourceControlManagementService implements SourceControlManagementService {

    private final List<SourceControlHandler> scmHandlers = new ArrayList<>();

    public DefaultSourceControlManagementService(List<SourceControlHandler> scmHandlers) {
        this.scmHandlers.addAll(scmHandlers);
    }

    @Override
    public ProjectHandle checkout(ProjectReference reference) throws IOException {

        SourceControlHandler handler = this.findHandler(reference)
                .orElseThrow(() -> {
                    String msg = String.format("No handler for %s available", reference.getUri());
                    return new UnsupportedProjectReference(msg, reference);
                });

        return handler.checkout(reference);
    }

    Optional<SourceControlHandler> findHandler(ProjectReference reference) {
        return this.scmHandlers.stream().filter(scm -> scm.canHandle(reference)).findFirst();
    }
}
