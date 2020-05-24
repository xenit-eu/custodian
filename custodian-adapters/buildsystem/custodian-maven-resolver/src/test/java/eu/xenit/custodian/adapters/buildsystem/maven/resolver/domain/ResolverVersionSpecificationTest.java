package eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResolverVersionSpecificationTest {

    /**
     * Test version range specifications
     *
     * @see <a href="http://maven.apache.org/enforcer/enforcer-rules/versionRanges.html">Version Range Specification</a>
     */
    static class VersionRangeSpecificationTests {

        @Test
        public void testUpperBoundInclusive() {
            // (,1.0] matches x <= 1.0
            var spec = ResolverVersionSpecification.from("(,1.0]");

            assertThat(spec.matches("1.0")).isTrue();
            assertThat(spec.matches("0.999.99.99.99")).isTrue();
            assertThat(spec.matches("1.0-SNAPSHOT")).isTrue();

            assertThat(spec.matches("1.0.1")).isFalse();
            assertThat(spec.matches("2.0")).isFalse();
        }

        @Test
        public void testUpperBoundExclusive() {
            // (,1.0) matches x <= 1.0
            var spec = ResolverVersionSpecification.from("(,1.0)");

            assertThat(spec.matches("0.999.99.99.99")).isTrue();
            assertThat(spec.matches("1.0-SNAPSHOT")).isTrue();

            assertThat(spec.matches("1.0")).isFalse();
        }

        @Test
        public void testSingleVersionRange() {
            // [1.0] matches x == 1.0
            var spec = ResolverVersionSpecification.from("[1.0]");

            assertThat(spec.matches("1.0")).isTrue();
            assertThat(spec.matches("1.0.0000.0.0")).isTrue();

            assertThat(spec.matches("2.0")).isFalse();
            assertThat(spec.matches("1.0-SNAPSHOT")).isFalse();

        }


        @Test
        public void testLowerBoundInclusive() {
            // [1.0,) matches x >= 1.0
            var spec = ResolverVersionSpecification.from("[1.0,)");

            assertThat(spec.matches("1.0")).isTrue();
            assertThat(spec.matches("1.0.1")).isTrue();
            assertThat(spec.matches("2.0")).isTrue();

            assertThat(spec.matches("1.0-SNAPSHOT")).isFalse();
        }

        @Test
        public void testLowerBoundExclusive() {
            // [(1.0,) matches x > 1.0
            var spec = ResolverVersionSpecification.from("(1.0,)");

            assertThat(spec.matches("1.0.1")).isTrue();
            assertThat(spec.matches("2.0")).isTrue();

            assertThat(spec.matches("1.0")).isFalse();
            assertThat(spec.matches("1.0-RC")).isFalse();
            assertThat(spec.matches("1.0-SNAPSHOT")).isFalse();
        }



        @Test
        public void testRangeExclusive() {
            // (1.0,2.0) matches  1.0 < x < 2.0
            var spec = ResolverVersionSpecification.from("(1.0,2.0)");

            assertThat(spec.matches("1.0.1")).isTrue();
            assertThat(spec.matches("2.0-RC")).isTrue();
            assertThat(spec.matches("2.0-SNAPSHOT")).isTrue();
            assertThat(spec.matches("1.123.123")).isTrue();


            assertThat(spec.matches("1.0")).isFalse();
            assertThat(spec.matches("1.0-SNAPSHOT")).isFalse();
            assertThat(spec.matches("2.0")).isFalse();
        }

        @Test
        public void testRangeInclusive() {
            // [1.0,2.0] matches  1.0 <= x <= 2.0
            var spec = ResolverVersionSpecification.from("[1.0,2.0]");

            assertThat(spec.matches("1.0")).isTrue();
            assertThat(spec.matches("2.0-RC")).isTrue();
            assertThat(spec.matches("2.0")).isTrue();
            assertThat(spec.matches("1.123.123")).isTrue();

            assertThat(spec.matches("1.0-SNAPSHOT")).isFalse();
            assertThat(spec.matches("2.0.1")).isFalse();
        }

        @Test
        public void testMultipleSets() {
            // (,1.0],[1.2,) matches x <= 1.0 or x >= 1.2
            var spec = ResolverVersionSpecification.from("(,1.0],[1.2,)");

            assertThat(spec.matches("0.9")).isTrue();
            assertThat(spec.matches("1.0")).isTrue();

            assertThat(spec.matches("1.0.1")).isFalse();
            assertThat(spec.matches("1.2-SNAPSHOT")).isFalse();

            assertThat(spec.matches("1.2")).isTrue();
            assertThat(spec.matches("2.0")).isTrue();
        }

        @Test
        public void excludeSpecificVersion() {
            // (,1.1),(1.1,) matches != 1.1
            var spec = ResolverVersionSpecification.from("(,1.1),(1.1,)");

            assertThat(spec.matches("1.0")).isTrue();
            assertThat(spec.matches("1.1-SNAPSHOT")).isTrue();

            assertThat(spec.matches("1.1")).isFalse();

            assertThat(spec.matches("1.1.1")).isTrue();
            assertThat(spec.matches("2.0")).isTrue();
        }





    }

}