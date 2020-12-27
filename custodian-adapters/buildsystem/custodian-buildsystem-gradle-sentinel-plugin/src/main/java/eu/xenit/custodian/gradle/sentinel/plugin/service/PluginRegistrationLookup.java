package eu.xenit.custodian.gradle.sentinel.plugin.service;

import eu.xenit.custodian.gradle.sentinel.plugin.support.ClassLoaderResourceResolver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import org.gradle.api.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginRegistrationLookup {

    private static final Logger log = LoggerFactory.getLogger(PluginRegistrationLookup.class);

    private Map<String, PluginRegistration> implClassLookup = new LinkedHashMap<>();
    private Map<String, PluginRegistration> pluginIdLookup = new LinkedHashMap<>();

    public PluginRegistrationLookup() {

    }

    public PluginRegistrationLookup loadPlugins(ClassLoader classLoader) {
        log.warn("!! loadPlugins with classloader: " + classLoader);
        try {
            var pluginResolver = new ClassLoaderResourceResolver(classLoader);
            var resources = pluginResolver.getResources("META-INF/gradle-plugins/*.properties")
                    .collect(Collectors.toList());

            log.warn("-- found "+resources.size()+" gradle plugin property files");
            resources.stream().map(this::loadPluginFromProperties)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(this::register);

            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

//    Optional<PluginRegistration> loadPluginFromProperties(String pluginId) {
//
//        String path = "META-INF/gradle-plugins/" + pluginId + ".properties";
//        URL url = PluginRegistrationLookup.class.getClassLoader().getResource(path);
//        return loadPluginFromProperties(url);
//    }

    Optional<PluginRegistration> loadPluginFromProperties(URL pluginPropertiesURL) {

        log.warn("loadPluginFromProperties("+pluginPropertiesURL+")");
        try (InputStream stream = pluginPropertiesURL.openStream()) {

            Properties properties = new Properties();
            properties.load(stream);

            String implementation = properties.getProperty("implementation-class");
            String pluginId = new File(pluginPropertiesURL.getPath()).getName();
            if (pluginId.endsWith(".properties")) {
                pluginId = pluginId.substring(0, pluginId.length() - ".properties".length());
            }

            return Optional.of(new PluginRegistration(pluginId, implementation));

        } catch (IOException e) {
            log.warn("Loading plugin from "+pluginPropertiesURL+" failed, skipping: " +  e.getMessage());
            return Optional.empty();
        }
    }

    public void register(PluginRegistration registration) {
        log.warn("-- building plugin index for plugin '"+registration.getImplementationClass()+"'");
        implClassLookup.put(registration.getImplementationClass(), registration);
        pluginIdLookup.put(registration.getId(), registration);
    }

    public Optional<PluginRegistration> lookupPluginId(Plugin<?> plugin) {
        return Optional.ofNullable(this.implClassLookup.get(plugin.getClass().getName()));
    }

    public Optional<PluginRegistration> lookupPluginByClass(Class<?> implementationClass) {
        return this.lookupPluginByClass(implementationClass.getName());

    }
    public Optional<PluginRegistration> lookupPluginByClass(String implementationClassName) {
        return Optional.ofNullable(this.implClassLookup.get(implementationClassName));

    }

    public static class PluginRegistration {

        private final String id;
        private final String implementationClass;

        PluginRegistration(String id, String implementationClass) {
            this.id = id;
            this.implementationClass = implementationClass;
        }

        public String getId() {
            return id;
        }

        public String getImplementationClass() {
            return implementationClass;
        }
    }

}
