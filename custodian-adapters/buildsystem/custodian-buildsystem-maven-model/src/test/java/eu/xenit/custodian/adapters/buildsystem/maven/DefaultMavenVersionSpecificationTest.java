package eu.xenit.custodian.adapters.buildsystem.maven;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification.VersionBounds;
import org.junit.Test;

public class DefaultMavenVersionSpecificationTest {

    @Test
    public void testSimpleVersion() {
        MavenVersionSpecification spec = new DefaultMavenVersionSpecification("1.2.3");

        assertThat(spec.isValid()).isTrue();
        assertThat(spec.isPinnedVersion()).isTrue();
        assertThat(spec.asPinnedVersion().isPresent()).isTrue();
        assertThat(spec.asPinnedVersion().get()).satisfies(v -> {
            assertThat(v.getValue()).isEqualTo("1.2.3");
            assertThat(v.getMajorVersion()).isEqualTo(1);
            assertThat(v.getMinorVersion()).isEqualTo(2);
            assertThat(v.getIncrementalVersion()).isEqualTo(3);
            assertThat(v.getBuildNumber()).isEqualTo(0);
            assertThat(v.getQualifier()).isNull();
        });
    }

    @Test
    public void testSnapshotVersion() {
        MavenVersionSpecification spec = new DefaultMavenVersionSpecification("1.2.3-SNAPSHOT");

        assertThat(spec.isValid()).isTrue();
        assertThat(spec.isPinnedVersion()).isTrue();
        assertThat(spec.asPinnedVersion().isPresent()).isTrue();
        assertThat(spec.asPinnedVersion().get()).satisfies(v -> {
            assertThat(v.getValue()).isEqualTo("1.2.3-SNAPSHOT");
            assertThat(v.getMajorVersion()).isEqualTo(1);
            assertThat(v.getMinorVersion()).isEqualTo(2);
            assertThat(v.getIncrementalVersion()).isEqualTo(3);
            assertThat(v.getBuildNumber()).isEqualTo(0);
            assertThat(v.getQualifier()).isEqualTo("SNAPSHOT");
        });
    }

    @Test
    public void testLowerBoundedRange() {
        MavenVersionSpecification spec = new DefaultMavenVersionSpecification("[1.2.3,)");

        assertThat(spec.isValid()).isTrue();
        assertThat(spec.isPinnedVersion()).isFalse();
        assertThat(spec.asPinnedVersion().isPresent()).isFalse();

        assertThat(spec.getBounds())
                .hasSize(1)
                .containsSequence(VersionBounds.fromLowerBounds("1.2.3", true));
    }

    @Test
    public void testUpperBoundedRange() {
        MavenVersionSpecification spec = new DefaultMavenVersionSpecification("(,1.2.3)");

        assertThat(spec.isValid()).isTrue();
        assertThat(spec.isPinnedVersion()).isFalse();
        assertThat(spec.asPinnedVersion().isPresent()).isFalse();

        assertThat(spec.getBounds())
                .hasSize(1)
                .containsSequence(VersionBounds.fromUpperBounds("1.2.3", false));
    }

    @Test
    public void testMultipleBounds() {
        MavenVersionSpecification spec = new DefaultMavenVersionSpecification("(,1.0],[1.2,)");

        assertThat(spec.getBounds())
                .hasSize(2)
                .containsSequence(
                        VersionBounds.fromUpperBounds("1.0", true),
                        VersionBounds.fromLowerBounds("1.2", true)
                );
    }
}