package eu.xenit.custodian.driver.test;

import eu.xenit.custodian.domain.entities.buildsystem.BuildSystemsCollection;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.domain.metadata.ProjectModelAssert;
import eu.xenit.custodian.domain.buildsystem.BuildSystemsContainer;
import java.util.Objects;
import org.assertj.core.api.AssertProvider;

public class ProjectModelAssertionTrait implements ProjectModel, AssertProvider<ProjectModelAssert> {

    private final ProjectModel projectModel;

    ProjectModelAssertionTrait(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    @Override
    public BuildSystemsCollection buildsystems() {
        Objects.requireNonNull(this.projectModel, "projectMetadata is null");
        return this.projectModel.buildsystems();
    }

    @Override
    public ProjectModelAssert assertThat() {
        return new ProjectModelAssert(this.projectModel);
    }
}
