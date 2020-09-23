package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import static eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file.GradleBuildParser.extractSection;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GradleBuildParserTest {

    public static final String GRADLE = String.join("\n",
            "plugins {",
            "    id 'eu.xenit.docker-alfresco' version '4.0.3'",
            "}",
            "",
            "group = 'com.example'",
            "version = '0.0.1-SNAPSHOT'",
            "",
            "dependencies {",
            "   alfrescoProvided \"org.alfresco:alfresco-enterprise:${alfrescoVersion}\"",
//                "}", // intentional whitespace
            "\t}", // intentional additional whitespace
            "");


    @Test
    public void testMissingPluginSection() {
        String empty = String.join("\n",
                "group = 'com.example'",
                "version = '0.0.1-SNAPSHOT'");

        String result = extractSection("plugins", empty);
        assertThat(result).isEmpty();
    }

    @Test
    public void testPluginSectionExtraction() {
        String result = extractSection("plugins", GRADLE);
        assertThat(result)
                .isNotBlank()
                .isEqualTo("id 'eu.xenit.docker-alfresco' version '4.0.3'");

    }

    @Test
    public void testDependenciesWithExtraCurlyBraces() {
        String result = extractSection("dependencies", GRADLE);
        assertThat(result)
                .isNotBlank()
                .isEqualTo("alfrescoProvided \"org.alfresco:alfresco-enterprise:${alfrescoVersion}\"");

    }

    @Test
    public void testUnbalancedBraces_shouldThrow() {
        String missingClosingBrace = String.join("\n",
                "plugins {",
                "    id 'eu.xenit.docker-alfresco' version '4.0.3'");

        Assertions.assertThrows(IllegalStateException.class, () -> {
            extractSection("plugins", missingClosingBrace);
        });


    }

    @Test
    public void testNested() {
        String gradle = String.join("\n",
                "sourceSets {",
                "   main {",
                "       amp {",
                "           dynamicExtension()",
                "       }",
                "   }",
                "}");

        String sourceSets = extractSection("sourceSets", gradle);
        String main = extractSection("main", sourceSets);
        String amp = extractSection("amp", main);

        assertThat(amp).isEqualTo("dynamicExtension()");
    }

    @Test
    public void testOnSingleLine() {
        String gradle = String.join("\n",
                "repositories {",
                "    mavenCentral()",
                "    maven { url 'https://artifacts.alfresco.com/nexus/content/groups/public/' }",
                "}",
                ""
        );

        String repositories = extractSection("repositories", gradle);
        String maven = extractSection("maven", repositories);

        assertThat(maven).isEqualTo("url 'https://artifacts.alfresco.com/nexus/content/groups/public/'");
    }

}