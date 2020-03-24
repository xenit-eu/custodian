package eu.xenit.custodian.domain.metadata;

import eu.xenit.custodian.domain.buildsystem.BuildAssert;
import eu.xenit.custodian.ports.spi.build.Build;
import eu.xenit.custodian.ports.api.ClonedRepositorySourceMetadata;
import eu.xenit.custodian.ports.spi.build.BuildSystem;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ClonedRepositorySourceMetadataAssert extends
        AbstractAssert<ClonedRepositorySourceMetadataAssert, ClonedRepositorySourceMetadata> {

    public ClonedRepositorySourceMetadataAssert(ClonedRepositorySourceMetadata clonedRepositorySourceMetadata) {
        super(clonedRepositorySourceMetadata, ClonedRepositorySourceMetadataAssert.class);
    }

    public static ClonedRepositorySourceMetadataAssert assertThat(
            ClonedRepositorySourceMetadata clonedRepositorySourceMetadata) {
        return new ClonedRepositorySourceMetadataAssert(clonedRepositorySourceMetadata);
    }

    public <B extends Build> ClonedRepositorySourceMetadataAssert hasBuildSystem(String buildSystemId,
            Class<B> buildType, Consumer<B> callback) {
        return this.hasBuildSystem(BuildSystem.forId(buildSystemId), buildType, callback);
    }

    public <B extends Build> ClonedRepositorySourceMetadataAssert hasBuildSystem(BuildSystem buildSystem,
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

    public ClonedRepositorySourceMetadataAssert hasBuildSystem(String id) {
        return this.hasBuildSystem(id, build -> {
        });
    }

    public ClonedRepositorySourceMetadataAssert hasBuildSystem(String id, Consumer<BuildAssert> callback) {
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
