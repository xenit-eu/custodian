package eu.xenit.custodian.domain.scm;

import eu.xenit.custodian.domain.project.ProjectHandle;
import eu.xenit.custodian.domain.project.ProjectReference;
import eu.xenit.custodian.ports.spi.scm.UnsupportedProjectReference;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.ObjectProvider;

public class CompositeSourceControlHandler implements SourceControlHandler {

    private final List<SourceControlHandler> scmHandlers = new ArrayList<>();

    public CompositeSourceControlHandler(Collection<SourceControlHandler> scmHandlers) {
        this.scmHandlers.addAll(scmHandlers);
    }

    public CompositeSourceControlHandler(Stream<SourceControlHandler> scmHandlers) {
        scmHandlers.forEach(this.scmHandlers::add);
    }

    @Override
    public boolean canHandle(ProjectReference reference) {
        return this.scmHandlers.stream().anyMatch(handler -> handler.canHandle(reference));
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

    public int size() {
        return this.scmHandlers.size();
    }

    public Stream<SourceControlHandler> handlers() {
        return this.scmHandlers.stream();
    }
}
