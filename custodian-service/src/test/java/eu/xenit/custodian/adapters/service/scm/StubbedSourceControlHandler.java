package eu.xenit.custodian.adapters.service.scm;

import eu.xenit.custodian.ports.api.SourceRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import java.util.function.Predicate;

public class StubbedSourceControlHandler implements SourceControlHandler {

    private final Predicate<SourceRepositoryReference> canHandlePredicate;
    private final SourceRepositoryHandle handle;

    public StubbedSourceControlHandler(SourceRepositoryHandle handle)
    {
        this((projectReference) -> true, handle);
    }

    public StubbedSourceControlHandler(Predicate<SourceRepositoryReference> canHandlePredicate, SourceRepositoryHandle handle)
    {
        this.canHandlePredicate = canHandlePredicate;
        this.handle = handle;
    }

    @Override
    public boolean canHandle(SourceRepositoryReference reference) {
        return this.canHandlePredicate.test(reference);
    }

    @Override
    public SourceRepositoryHandle checkout(SourceRepositoryReference reference) {
        return this.handle;
    }
}
