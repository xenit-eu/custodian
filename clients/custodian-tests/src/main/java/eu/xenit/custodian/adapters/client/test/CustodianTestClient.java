package eu.xenit.custodian.adapters.client.test;

import eu.xenit.custodian.domain.ProjectMetadata;
import eu.xenit.custodian.domain.project.ProjectHandle;
import eu.xenit.custodian.domain.project.ProjectReference;
import eu.xenit.custodian.ports.api.Custodian;
import eu.xenit.custodian.ports.spi.metadata.MetadataAnalyzerException;
import java.io.IOException;
import java.nio.file.Path;

public class CustodianTestClient {

    private final Custodian custodian;

    public CustodianTestClient(Custodian custodian) {
        this.custodian = custodian;
    }

    public ProjectReference createReference(Path location) {
        return this.custodian.createReference(location.toString());
    }

    public ProjectMetadataAssertionTrait extractMetadata(Path location) throws IOException, MetadataAnalyzerException {
        ProjectReference reference = this.createReference(location);
        ProjectHandle handle = this.custodian.checkoutProject(reference);
        return this.extractMetadata(handle);

    }

    public ProjectMetadataAssertionTrait extractMetadata(ProjectHandle handle) throws MetadataAnalyzerException {
        ProjectMetadata projectMetadata = this.custodian.extractMetadata(handle);
        return new ProjectMetadataAssertionTrait(projectMetadata);
    }


}
