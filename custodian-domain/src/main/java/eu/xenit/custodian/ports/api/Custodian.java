package eu.xenit.custodian.ports.api;

import eu.xenit.custodian.domain.usecases.changes.LogicalChangeSet;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import java.io.IOException;

public interface Custodian {

    SourceRepositoryReference createReference(String repositoryReference);
    ClonedRepositoryHandle checkoutProject(SourceRepositoryReference reference) throws IOException;

    // we probably want to expose a simplified ProjectModel (especially the Build interface)
    ProjectModel analyzeProjectModel(ClonedRepositoryHandle clonedRepositoryHandle);
    LogicalChangeSet getChanges(ProjectModel projectModel);


    //    createPullRequest(ClonedRepositoryHandle clonedRepositoryHandle);
//    ProjectUpdateResult run(SourceRepositoryReference sourceRepositoryReference);
}
