package eu.xenit.custodian.domain;

import eu.xenit.custodian.domain.metadata.ProjectMetadata;
import eu.xenit.custodian.domain.metadata.buildsystem.Build;
import eu.xenit.custodian.domain.metadata.buildsystem.BuildAssert;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;

public class ProjectMetadataAssert extends AbstractAssert<ProjectMetadataAssert, ProjectMetadata> {

    public ProjectMetadataAssert(ProjectMetadata projectMetadata) {
        super(projectMetadata, ProjectMetadataAssert.class);
    }

    public static ProjectMetadataAssert assertThat(ProjectMetadata projectMetadata) {
        return new ProjectMetadataAssert(projectMetadata);
    }

    public ProjectMetadataAssert hasBuildSystem(String id) {
        return this.hasBuildSystem(id, build -> { });
    }

    public ProjectMetadataAssert hasBuildSystem(String id, Consumer<BuildAssert> callback) {
        Objects.requireNonNull(id, "Argument 'id' can not be null");

        Build build = this.actual.buildsystems().get(id);

        if (Objects.isNull(build)) {
            failWithMessage("Expected project to contain build system <%s> but was: <%s>", id,
                    String.join(", ", this.actual.buildsystems().ids().collect(Collectors.toList())));
        }

        callback.accept(new BuildAssert(build));

        return this;
    }
}
