package eu.xenit.custodian.adapters.channel.mavenrepo;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionSpecification;
import eu.xenit.custodian.domain.buildsystem.VersionSpecification;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.junit.Test;

public class MavenVersionRangeCalculatorTest {

    private MavenVersionRangeCalculator versionRangeCalculator = new MavenVersionRangeCalculator();

    @Test
    public void getMajorUpdateVersionRange() {
        VersionSpecification range = MavenVersionRangeCalculator
                .getMajorUpdateVersionRange(MavenModuleVersion.from("1.2.3"));

        assertThat(range.getValue())
                .isNotNull()
                .isEqualTo("[1.2.3,)");
    }

    @Test
    public void getMinorUpdateVersionRange() {
        VersionSpecification range = MavenVersionRangeCalculator
                .getMinorUpdateVersionRange(MavenModuleVersion.from("1.2.3"));

        assertThat(range.toString())
                .isNotNull()
                .isEqualTo("[1.2.3,2)");
    }

    @Test
    public void getIncrementalVersionRange() {
        VersionSpecification range = MavenVersionRangeCalculator
                .getIncrementalVersionRange(MavenModuleVersion.from("1.2.3"));

        assertThat(range.toString())
                .isNotNull()
                .isEqualTo("[1.2.3,1.3)");
    }
}