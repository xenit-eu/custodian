package eu.xenit.custodian.adapters.client.test;

import eu.xenit.custodian.domain.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.ProjectMetadataAssert;
import eu.xenit.custodian.domain.metadata.buildsystem.BuildSystemsContainer;
import eu.xenit.custodian.domain.project.ProjectReference;
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
