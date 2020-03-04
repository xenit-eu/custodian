package eu.xenit.custodian.adapters.client.test;

import eu.xenit.custodian.ports.api.ProjectMetadata;
import eu.xenit.custodian.domain.metadata.ProjectMetadataAssert;
import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.ports.api.ProjectReference;
import java.util.Objects;
import org.assertj.core.api.AssertProvider;

public class ProjectMetadataAssertionTrait implements ProjectMetadata, AssertProvider<ProjectMetadataAssert> {

    private final ProjectMetadata projectMetadata;

    ProjectMetadataAssertionTrait(ProjectMetadata projectMetadata) {
        this.projectMetadata = projectMetadata;
    }

    @Override
    public ProjectReference getReference() {
        Objects.requireNonNull(this.projectMetadata, "projectMetadata is null");
        return this.projectMetadata.getReference();
    }

    @Override
    public BuildSystemsContainer buildsystems() {
        Objects.requireNonNull(this.projectMetadata, "projectMetadata is null");
        return this.projectMetadata.buildsystems();
    }

    @Override
    public ProjectMetadataAssert assertThat() {
        return new ProjectMetadataAssert(this.projectMetadata);
    }
}
