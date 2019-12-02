package eu.xenit.custodian.adapters.metadata.gradle;


import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.metadata.gradle.notation.GradleDependencyNotationParserDelegate;
import eu.xenit.custodian.adapters.buildsystem.maven.notation.NotationParserBuilder;
import eu.xenit.custodian.adapters.buildsystem.maven.notation.DependencyNotationConverter;
import eu.xenit.custodian.adapters.buildsystem.maven.notation.StringNotationParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AssertProvider;
import org.junit.Test;

public class NotationParserBuilderTest {

    @Test
    public void testSimpleNotation() {
        StringNotationParser<TestDependencyNotation> parser = new NotationParserBuilder()
                .build(new GradleDependencyNotationParserDelegate(), new TestDependencyConverter());

        parser.apply("org.apache.httpcomponents:httpclient:4.5.1")
                .assertThat()
                .hasGroup("org.apache.httpcomponents")
                .hasName("httpclient")
                .hasVersion("4.5.1")
                .hasNoClassifier()
                .hasNoType();
    }

    @Test
    public void testClassifier() {
        StringNotationParser<TestDependencyNotation> parser = new NotationParserBuilder()
                .build(new GradleDependencyNotationParserDelegate(), new TestDependencyConverter());

        parser.apply("org.apache.httpcomponents:httpclient:4.5.1:sources")
                .assertThat()
                .hasGroup("org.apache.httpcomponents")
                .hasName("httpclient")
                .hasVersion("4.5.1")
                .hasClassifier("sources")
                .hasNoType();
    }

    @Test
    public void testExtension() {
        StringNotationParser<TestDependencyNotation> parser = new NotationParserBuilder()
                .build(new GradleDependencyNotationParserDelegate(), new TestDependencyConverter());

        parser.apply("org.alfresco:alfresco-share-services:6.0.c@amp")
                .assertThat()
                .hasGroup("org.alfresco")
                .hasName("alfresco-share-services")
                .hasVersion("6.0.c")
                .hasNoClassifier()
                .hasType("amp");
    }

    class TestDependencyConverter implements DependencyNotationConverter<TestDependencyNotation> {

        @Override
        public TestDependencyNotation from(String group, String name, String version,
                String classifier, String type) {
            return new TestDependencyNotation(group, name, version, classifier, type);
        }
    }

    @Getter
    @AllArgsConstructor
    class TestDependencyNotation implements AssertProvider<TestDependencyNotationAssert> {

        private String group;
        private String name;
        private String version;
        private String classifier;
        private String type;

        @Override
        public TestDependencyNotationAssert assertThat() {
            return new TestDependencyNotationAssert(this);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.getGroup()).append(":").append(this.getName());

            if (this.getVersion() != null) {
                sb.append(":").append(this.getVersion());

            }

            if (this.getClassifier() != null) {
                if (this.getVersion() == null) {
                    sb.append(":");
                }
                sb.append(this.getClassifier());
            }

            if (this.getType() != null) {
                sb.append("@").append(this.getType());
            }

            return sb.toString();
        }
    }

    class TestDependencyNotationAssert extends
            AbstractAssert<TestDependencyNotationAssert, TestDependencyNotation> {


        public TestDependencyNotationAssert(
                TestDependencyNotation testDependencyNotation) {
            super(testDependencyNotation, TestDependencyNotationAssert.class);
        }

        public TestDependencyNotationAssert hasGroup(String group) {
            assertThat(this.actual.getGroup()).as("group in %s", this.actual).isEqualTo(group);
            return this;
        }

        public TestDependencyNotationAssert hasName(String name) {
            assertThat(this.actual.getName()).as("name").isEqualTo(name);
            return this;
        }

        public TestDependencyNotationAssert hasVersion(String version) {
            assertThat(this.actual.getVersion()).as("version").isEqualTo(version);
            return this;
        }

        public TestDependencyNotationAssert hasClassifier(String classifier) {
            assertThat(this.actual.getClassifier()).as("classifier").isEqualTo(classifier);
            return this;
        }

        public TestDependencyNotationAssert hasNoClassifier() {
            assertThat(this.actual.getClassifier()).as("empty classifier").isNull();
            return this;
        }

        public TestDependencyNotationAssert hasType(String type) {
            assertThat(this.actual.getType()).as("type").isEqualTo(type);
            return this;
        }

        public TestDependencyNotationAssert hasNoType() {
            assertThat(this.actual.getType()).as("empty type").isNull();
            return this;
        }
    }
}