package eu.xenit.custodian.adapters.channel.mavenrepo;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenModuleVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.MavenVersionRangeCalculator;
import eu.xenit.custodian.domain.buildsystem.VersionSpecification;
import org.junit.Test;

public class MavenVersionRangeCalculatorTest {

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