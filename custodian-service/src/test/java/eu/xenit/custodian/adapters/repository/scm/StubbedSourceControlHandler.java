package eu.xenit.custodian.adapters.repository.scm;

import eu.xenit.custodian.domain.model.ProjectHandle;
import eu.xenit.custodian.domain.model.ProjectReference;
import eu.xenit.custodian.domain.repository.scm.SourceControlHandler;
import java.util.function.Predicate;

public class StubbedSourceControlHandler implements SourceControlHandler {

    private final Predicate<ProjectReference> canHandlePredicate;
    private final ProjectHandle handle;

    public StubbedSourceControlHandler(ProjectHandle handle)
    {
        this((projectReference) -> true, handle);
    }

    public StubbedSourceControlHandler(Predicate<ProjectReference> canHandlePredicate, ProjectHandle handle)
    {
        this.canHandlePredicate = canHandlePredicate;
        this.handle = handle;
    }

    @Override
    public boolean canHandle(ProjectReference reference) {
        return this.canHandlePredicate.test(reference);
    }

    @Override
    public ProjectHandle checkout(ProjectReference reference) {
        return this.handle;
    }
}
