package eu.xenit.custodian.driver.test;

import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.domain.usecases.changes.LogicalChangeSet;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import java.io.IOException;
import java.nio.file.Path;

public class CustodianTestClient {

    private final Custodian custodian;

    public CustodianTestClient(Custodian custodian) {
        this.custodian = custodian;
    }

    public SourceRepositoryReference createReference(Path location) {
        return this.custodian.createReference(location.toString());
    }

    public ClonedRepositoryHandle checkout(SourceRepositoryReference reference) throws IOException {
        return this.custodian.checkoutProject(reference);
    }

    public ClonedRepositoryHandle checkout(Path location) throws IOException {
        return this.custodian.checkoutProject(this.createReference(location));
    }

    // TODO fix argument
    public ProjectModelAssertionTrait getProjectModel(ClonedRepositoryHandle handle) {
        ProjectModel projectModel = this.custodian.analyzeProjectModel(handle);
        return new ProjectModelAssertionTrait(projectModel);
    }

    public ChangeSetAssertionTrait getChanges(ClonedRepositorySourceMetadata metadata) {
        LogicalChangeSet changes = this.custodian.getChanges(metadata);
        return new ChangeSetAssertionTrait(changes);
    }


}
