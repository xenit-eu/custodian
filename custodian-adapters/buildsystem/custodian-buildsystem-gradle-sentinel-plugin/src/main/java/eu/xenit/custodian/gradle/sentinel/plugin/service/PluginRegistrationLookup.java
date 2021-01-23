package eu.xenit.custodian.gradle.sentinel.plugin.service;

import eu.xenit.custodian.gradle.sentinel.plugin.support.ClassLoaderResourceResolver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.stream.Collectors;
import org.gradle.api.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginRegistrationLookup {

    private static final Logger log = LoggerFactory.getLogger(PluginRegistrationLookup.class);

    private Map<String, PluginRegistration> implClassLookup = new LinkedHashMap<>();
    private Map<String, PluginRegistration> pluginCache = new LinkedHashMap<>();

    public PluginRegistrationLookup() {

    }

    public PluginRegistrationLookup loadPlugins(ClassLoader classLoader) {
        try {
            var pluginResolver = new ClassLoaderResourceResolver(classLoader);
            var resources = pluginResolver.getResources("META-INF/gradle-plugins/*.properties")
                    .collect(Collectors.toList());

            resources.stream().map(url -> this.loadPluginFromProperties(url, classLoader))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(this::cache);

            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Optional<PluginRegistration> loadPluginFromProperties(URL pluginPropertiesURL, ClassLoader classLoader) {
        try (InputStream stream = pluginPropertiesURL.openStream()) {

            Properties properties = new Properties();
            properties.load(stream);

            String implementation = properties.getProperty("implementation-class");
            String pluginId = new File(pluginPropertiesURL.getPath()).getName();
            if (pluginId.endsWith(".properties")) {
                pluginId = pluginId.substring(0, pluginId.length() - ".properties".length());
            }

            String version = getPluginVersion(classLoader, implementation, pluginPropertiesURL);
            return Optional.of(new PluginRegistration(pluginId, implementation, version));

        } catch (IOException e) {
            log.warn("Loading plugin from " + pluginPropertiesURL + " failed, skipping: " + e.getMessage());
            return Optional.empty();
        }
    }

    private String getPluginVersion(ClassLoader classLoader, String implementation, URL propertiesUrl) {
        // try to load version from Package
        try {
            Package pluginPackage = classLoader.loadClass(implementation).getPackage();
            if (pluginPackage != null && pluginPackage.getImplementationVersion() != null) {
                return pluginPackage.getImplementationVersion();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // fallback to searching .jar META-INF/MANIFEST.MF

        try {
            log.trace("Trying to load META-INF/MANIFEST.MF for " + implementation + " from " + propertiesUrl+" protocol: "+propertiesUrl.getProtocol());
            if (ClassLoaderResourceResolver.isJarURL(propertiesUrl)) {

                var connection = (JarURLConnection) propertiesUrl.openConnection();
                var manifest = connection.getManifest();
                String version = manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
                if (version != null) {
                    log.trace("Version from manually loading .jar manifest: "+version);
                    return version;
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        log.warn("Version for " + implementation + " not found -> "+propertiesUrl);
        return null;
    }

    private void cache(PluginRegistration registration) {
        PluginRegistration old = implClassLookup.putIfAbsent(registration.getImplementationClass(), registration);
        if (old == null) {
            pluginCache.put(registration.getId(), registration);
            log.debug("Caching '" + registration.getId() + "' implementation " + registration.getImplementationClass());
        } else {
            log.debug("Plugin " + old.getId() + " was already cached");
        }
    }

    public Optional<PluginRegistration> lookupPluginId(Plugin<?> plugin) {
        return Optional.ofNullable(this.implClassLookup.get(plugin.getClass().getName()));
    }

    public Optional<PluginRegistration> lookupPluginByClass(Class<?> implementationClass) {
        if (implementationClass == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }

        Optional<PluginRegistration> optional = this.lookupPluginByClass(implementationClass.getName());
        if (optional.isEmpty()) {
            try {
                // not found in lookup cache
                ClassLoader classLoader = implementationClass.getClassLoader();
                log.trace("lookupPluginByClass(" + implementationClass.getName()
                        + ") not found - resolve from classloader: " + classLoader);

                var pluginResolver = new ClassLoaderResourceResolver(classLoader);
                var resources = pluginResolver.getResources("META-INF/gradle-plugins/*.properties")
                        .collect(Collectors.toList());

                String implClassName = implementationClass.getName();
                return resources.stream()
                        //.peek(url -> log.warn("-- found " + url))
                        .map(url -> this.loadPluginFromProperties(url, classLoader))
                        .filter(Optional::isPresent)
                        .map(Optional::get)

                        // cache the lookups for all the plugin-registrations we found
                        .peek(this::cache)

                        // terminal operation to consume the stream fully,
                        // so all found plugin-registrations get cached
                        .collect(Collectors.toList()).stream()

                        // filter out the plugin that matches the implementationClass
                        .filter(plugin -> plugin.getImplementationClass().equals(implClassName))

                        // and return the matching plugin
                        .findAny();
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }
        return optional;
    }

    public Optional<PluginRegistration> lookupPluginByClass(String implementationClassName) {
        return Optional.ofNullable(this.implClassLookup.get(implementationClassName));

    }

    public static class PluginRegistration {

        private final String id;
        private final String implementationClass;
        private final String version;

        PluginRegistration(String id, String implementationClass, String version) {
            this.id = id;
            this.implementationClass = implementationClass;
            this.version = version;
        }

        public String getId() {
            return id;
        }

        public String getImplementationClass() {
            return implementationClass;
        }

        public String getVersion() {
            return version;
        }
    }

}
