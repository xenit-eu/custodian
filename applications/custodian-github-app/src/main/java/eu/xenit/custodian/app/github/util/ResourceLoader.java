package eu.xenit.custodian.app.github.util;

import eu.xenit.custodian.app.github.adapters.GitHubAppJwtProvider;
import eu.xenit.custodian.util.Arguments;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public final class ResourceLoader {

    /**
     * Pseudo URL prefix for loading from the class path: "classpath:".
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * URL prefix for loading from the file system: "file:".
     */
    public static final String FILE_URL_PREFIX = "file:";


    private ResourceLoader() {

    }

    /**
     * Resolve the given resource location to a {@code java.net.URL}.
     * <p>Does not check whether the URL actually exists; simply returns
     * the URL that the given location would correspond to.
     *
     * In case a file path starts with the character `~`, the tilde is expanded
     * first to the user home folder given by the system property 'user.home'.
     *
     * @param resourceLocation the resource location to resolve: either a "classpath:" pseudo URL, or a "file:" URL, or
     * a plain file path.
     * @return a corresponding URL object
     * @throws FileNotFoundException if the resource cannot be resolved to a URL
     */
    public static URL getURL(String resourceLocation) throws FileNotFoundException {
        Arguments.notNull(resourceLocation, "resourceLocation");

        // try resolve classpath:-resources from current thread context classloader
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Objects.requireNonNull(cl, "ClassLoader is null");
            URL url = cl.getResource(resourceLocation.substring(CLASSPATH_URL_PREFIX.length()));
            if (url == null) {
                throw new FileNotFoundException(resourceLocation);
            }
            return url;
        }

        // fallback through to normal URL
        try {
            // try URL
            return new URL(resourceLocation);
        } catch (MalformedURLException ignored) {
            // fallback to normal file
            try {
                // Expand '~' to the user home folder
                if (resourceLocation.startsWith("~")) {
                    resourceLocation = System.getProperty("user.home") + resourceLocation.substring(1);
                }
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException ex2) {
                throw new FileNotFoundException(resourceLocation);
            }
        }
    }

    public static InputStream getInputStream(String resourceLocation) throws IOException {
        return getURL(resourceLocation).openStream();
    }
}
