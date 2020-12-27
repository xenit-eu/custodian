package eu.xenit.custodian.gradle.sentinel.plugin.support;

import org.gradle.api.plugins.JavaPlugin;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClassLoaderResourceResolverTest {

    @Test
    void testResolveDirect() throws IOException {

        ClassLoader cl = JavaPlugin.class.getClassLoader();
        ClassLoaderResourceResolver resolver = new ClassLoaderResourceResolver(cl);
        //List<URL> result = resolver.getResources("META-INF/gradle-plugins/org.gradle.java.properties").collect(Collectors.toList());
        List<URL> result = resolver.getResources("META-INF/gradle-plugins/").collect(Collectors.toList());
        assertThat(result).hasSize(1);
    }

    @Test
    void findRoots() throws IOException {
        ClassLoaderResourceResolver resolver = new ClassLoaderResourceResolver();
        var result = resolver.getResources("").collect(Collectors.toList());

        assertThat(result)
                .isNotEmpty()
                .as("Contains gradle-api jar")
                .anyMatch(url ->
                        url.toString().startsWith("jar:file:")
                                && url.toString().contains("gradle-api-")
                                && url.toString().endsWith(".jar!/"));
    }

    @Test
    void findGradlePluginProperties() throws IOException {

        ClassLoader cl = JavaPlugin.class.getClassLoader();
        ClassLoaderResourceResolver resolver = new ClassLoaderResourceResolver(cl);
        List<URL> result = resolver.getResources("META-INF/gradle-plugins/*.properties").collect(Collectors.toList());

        assertThat(result)
                .extracting(URL::toString)

                // check some standard gradle plugins that should be loaded from gradle-api.jar
                .anySatisfy(url -> assertThat(url).endsWith("/org.gradle.base.properties"))
                .anySatisfy(url -> assertThat(url).endsWith("/org.gradle.java.properties"))
                .anySatisfy(url -> assertThat(url).endsWith("/org.gradle.java-library.properties"))

                // check another gradle plugin (on the testRuntime classpath)
                .anySatisfy(url -> assertThat(url).endsWith("/io.spring.dependency-management.properties"))

                // check our sentinel-plugin, should be loaded from a file location (build/resources/)
                // in this project, not a .jar
                .anySatisfy(url -> assertThat(url).endsWith("/eu.xenit.custodian.sentinel.properties"));


    }
}