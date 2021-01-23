package eu.xenit.custodian.gradle.sentinel.plugin.service;

// import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import org.gradle.api.plugins.JavaPlugin;
import org.junit.jupiter.api.Test;


class PluginRegistrationLookupTest {

//    @Test
//    void testLoadGradleWrapperPluginProperties () {
//        PluginRegistrationLookup pluginRegistry = new PluginRegistrationLookup();
//        PluginRegistration plugin = pluginRegistry.loadPluginFromProperties("org.gradle.wrapper");
//
//        assertThat(plugin.getId()).isEqualTo("org.gradle.wrapper");
//        assertThat(plugin.getImplementationClass()).isEqualTo("org.gradle.buildinit.plugins.WrapperPlugin");
//    }

    @Test
    void testCollectGradlePluginProperties() throws IOException {
        PluginRegistrationLookup registry = new PluginRegistrationLookup();
        registry.loadPlugins(Thread.currentThread().getContextClassLoader());


        assertThat(registry.lookupPluginByClass(JavaPlugin.class))
                .hasValueSatisfying(plugin -> assertThat(plugin.getId()).isEqualTo("org.gradle.java"));
    }

    @Test
    void getScalaPluginVersionFromManifest() throws IOException {
        URL propertiesUrl = this.getClass().getClassLoader().getResource("META-INF/gradle-plugins/org.gradle.scala.properties");
        assertThat(propertiesUrl).isNotNull();
        assertThat(propertiesUrl.getProtocol()).isEqualTo("jar");
        assertThat(propertiesUrl.toString()).startsWith("jar:file");

        // open jar-url-connection
        var connection = (JarURLConnection) propertiesUrl.openConnection();
        var manifest = connection.getManifest();
    }

}