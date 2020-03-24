package eu.xenit.custodian.adapters.service.scm;

import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.util.function.Predicate;

public class StubbedSourceControlHandler implements SourceControlHandler {

    private final Predicate<SourceRepositoryReference> canHandlePredicate;
    private final ClonedRepositoryHandle handle;

    public StubbedSourceControlHandler(ClonedRepositoryHandle handle)
    {
        this((projectReference) -> true, handle);
    }

    public StubbedSourceControlHandler(Predicate<SourceRepositoryReference> canHandlePredicate, ClonedRepositoryHandle handle)
    {
        this.canHandlePredicate = canHandlePredicate;
        this.handle = handle;
    }

    @Override
    public boolean canHandle(SourceRepositoryReference reference) {
        return this.canHandlePredicate.test(reference);
    }

    @Override
    public ClonedRepositoryHandle checkout(SourceRepositoryReference reference) {
        return this.handle;
    }
}
