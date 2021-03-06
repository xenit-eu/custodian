package eu.xenit.custodian.adapters.gradle.buildsystem.asserts.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GradlePluginsStringAssertTest {

    @Test
    public void testPluginAsserts() {
        GradlePluginsStringAssert.from(GradleBuildParserTest.GRADLE)
                .hasPlugin("eu.xenit.docker-alfresco", "4.0.3")
                .hasPlugin("eu.xenit.docker-alfresco")
                .doesNotHavePlugin("eu.xenit.alfresco");
    }

    @Test
    public void hasPlugin_expectFailure() {
        Assertions.assertThrows(AssertionError.class, () ->
                GradlePluginsStringAssert.from(GradleBuildParserTest.GRADLE).hasPlugin("eu.xenit.alfresco"));
    }

    @Test
    public void doesNotHavePlugin_expectFailure() {
        Assertions.assertThrows(AssertionError.class, () ->
                GradlePluginsStringAssert.from(GradleBuildParserTest.GRADLE)
                        .doesNotHavePlugin("eu.xenit.docker-alfresco"));
    }
}