package eu.xenit.custodian.gradle.sentinel.plugin.service;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.gradle.sentinel.plugin.service.PluginRegistrationLookup.PluginRegistration;
import eu.xenit.custodian.gradle.sentinel.plugin.support.ClassLoaderResourceResolver;
import java.io.IOException;
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
}