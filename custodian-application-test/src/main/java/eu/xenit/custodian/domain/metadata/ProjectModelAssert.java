package eu.xenit.custodian.domain.metadata;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.GradleBuildSystem;
import eu.xenit.custodian.adapters.gradle.buildsystem.asserts.model.GradleBuildAssert;
import eu.xenit.custodian.domain.usecases.analysis.ports.ProjectModel;
import eu.xenit.custodian.ports.spi.buildsystem.Build;
import eu.xenit.custodian.ports.spi.buildsystem.BuildSystem;
import java.util.Objects;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ProjectModelAssert extends
        AbstractAssert<ProjectModelAssert, ProjectModel> {

    public ProjectModelAssert(ProjectModel projectModel) {
        super(projectModel, ProjectModelAssert.class);
    }

    public static ProjectModelAssert assertThat(
            ProjectModel projectModel) {
        return new ProjectModelAssert(projectModel);
    }

    public <B extends Build> ProjectModelAssert hasBuildSystem(String buildSystemId,
            Class<B> buildType, Consumer<B> callback) {
        return this.hasBuildSystem(BuildSystem.forId(buildSystemId), buildType, callback);
    }

    public <B extends Build> ProjectModelAssert hasBuildSystem(BuildSystem buildSystem,
            Class<B> buildType, Consumer<B> callback) {
        Objects.requireNonNull(buildSystem, "Argument 'buildSystem' is required");
        Objects.requireNonNull(buildType, "Argument 'buildType' is required");
        Objects.requireNonNull(callback, "Argument 'callback' is required");

        // assert that build-type argument matches the build-system
        Assertions.assertThat(buildType)
                .as("Build type '%s' does not match build system %s, expected %s",
                        buildType, buildSystem, buildSystem.getBuildType())
                .isEqualTo(buildSystem.getBuildType());

        Build build = this.actual.buildsystems().get(buildSystem.id());
        Assertions.assertThat(build)
                .as("Build system %s is not present", buildSystem.id())
                .isNotNull()

                .as("Build is not of the expected type %s", buildType)
                .isInstanceOf(buildType);

        callback.accept(buildType.cast(build));

        return myself;

    }

    public ProjectModelAssert assertGradleBuild(Consumer<GradleBuildAssert> callback) {
        return this.hasBuildSystem(GradleBuildSystem.ID, GradleBuild.class, gradleBuild -> {
            callback.accept(new GradleBuildAssert(gradleBuild));
        });
    }
}
