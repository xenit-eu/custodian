package eu.xenit.custodian.gradle.sentinel.plugin.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resolves resources from a provided class loader, supporting wildcards in the resource name.
 * <p>
 * This implementation is loosely inspired by Spring PathMatchingResourcePatternResolver, but has a slightly different
 * approach to take into account that jar files do not need explicit entries for directories. That subtle difference
 * make this Spring resolver not be able to resolve patterns like /META-INF/*.properties if /META-INF/ is implicit.
 * <p>
 * To work around the issue
 */
public class ClassLoaderResourceResolver {

    private static final Logger log = LoggerFactory.getLogger(ClassLoaderResourceResolver.class);

    private final ClassLoader classLoader;
    private final PathMatcher pathMatcher;

    private static final List<Character> WILDCARD_CHARS = Arrays.asList('*');

    /**
     * Separator between JAR URL and file path within the JAR: "!/".
     */
    public static final String JAR_URL_SEPARATOR = "!/";

    public ClassLoaderResourceResolver() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ClassLoaderResourceResolver(ClassLoader classLoader) {
        this(classLoader, new AntPathMatcherAdapter());
    }

    public ClassLoaderResourceResolver(ClassLoader classLoader, PathMatcher pathMatcher) {
        Objects.requireNonNull(classLoader, "classLoader must not be null");
        Objects.requireNonNull(pathMatcher, "pathMatcher must not be null");
        this.classLoader = classLoader;
        this.pathMatcher = pathMatcher;
    }

    public Stream<URL> getResources(String locationPattern) throws IOException {
        Objects.requireNonNull(locationPattern, "Location pattern must not be null");
        if (this.pathMatcher.isPattern(locationPattern)) {
            return this.findPatternMatchingResources(locationPattern);
        } else {
            // no wildcards, return exact matches
            return this.findClassPathResources(locationPattern);
        }
    }

    private Stream<URL> findClassPathResources(String location) throws IOException {
        // strip leading slash
        String path = location;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        var result = new LinkedHashSet<URL>();

        asStream(this.classLoader.getResources(path)).forEach(result::add);

        if ("".equals(path)) {
            // Because .jar files usually don't have entries for the root directory, they are
            // likely to be missing from the result above.
            // Compensate by searching the classloader hierarchy for .jar references and add them to
            // the result in the form of pointers to the root of the jar file
            result.addAll(addAllClassLoaderJarRoots(this.classLoader));
        }

        return result.stream();
    }

    /**
     * Traverse class loader hierarchy to find all {@link URLClassLoader} URLs for jar file references
     *
     * @param classLoader the ClassLoader to search (including its ancestors)
     * @return the set of URLs with jar file references
     */
    private LinkedHashSet<URL> addAllClassLoaderJarRoots(ClassLoader classLoader) {
        LinkedHashSet<URL> result;

        if (log.isTraceEnabled()) {
            log.trace("Collecting roots from class-loader " + classLoader.getName() + " [class:" + classLoader.getClass().getName() + " is-url-cl:" + (classLoader instanceof URLClassLoader) + "]");
        }

        // gather URLs from parent classloader
        var parent = classLoader.getParent();
        if (parent != null) {
            result = this.addAllClassLoaderJarRoots(parent);
        } else {
            result = new LinkedHashSet<>();
        }

        // add the jar references we cant find for the current classloader
        if (classLoader instanceof URLClassLoader) {
            URL[] urls = ((URLClassLoader) classLoader).getURLs();
            for (URL url : urls) {
                if (urlResourceExists(url)) {
                    log.trace("-- found .jar "+url+" in cl "+classLoader.getName() + " [class:"+classLoader.getClass().getName()+"]");
                    result.add(url);
                }
            }
        }

        if (classLoader == ClassLoader.getSystemClassLoader()) {
            // "java.class.path" manifest evaluation...
            result.addAll(findJavaClassPathJarReferences());
        }

        return result;
    }

    /**
     * Find jar file references from the "java.class.path" system property and return the result set in the form of
     * pointers to the root of the jar file content.
     *
     * @return the set of jar roots gathered from the java.class.path property
     * @since 4.3
     */
    protected LinkedHashSet<URL> findJavaClassPathJarReferences() {
        LinkedHashSet<URL> result = new LinkedHashSet<>();
        try {

            String javaClassPathProperty = System.getProperty("java.class.path");
            log.trace("searching java.class.path for .jar references: "+javaClassPathProperty);
            for (String path : javaClassPathProperty.split(System.getProperty("path.separator"))) {
//                try {
                File file = new File(path).getAbsoluteFile();
                String filePath = file.getAbsolutePath();
                if (file.exists() && filePath.toLowerCase().endsWith(".jar")) {
                    URL url = new URL("jar:file:" + filePath + "!/");
                    result.add(url);
                }
            }
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to evaluate 'java.class.path' manifest entries: " + ex);
            }
        }
        return result;
    }


    private static boolean urlResourceExists(URL url) {

        if (url.getProtocol().equals("file")) {
            try {
                URI uri = new URI(url.toString());
                File file = new File(uri.getSchemeSpecificPart());
                return file.exists();
            } catch (URISyntaxException e) {
                return false;
            }
        } else {
            // Fall back to stream existence: can we open the stream?
            try (InputStream ignored = url.openStream()) {
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    /**
     * Find all resources that match the given location pattern.
     * <p>
     * Supports resources in jar files and zip file and in the file system.
     */
    private Stream<URL> findPatternMatchingResources(String locationPattern) throws IOException {
        // find all classloader roots
        return this.findClassPathResources("")

                // try to enumerate everything under the root, retain what matches the location pattern
                .flatMap(rootDirUrl -> {
                    if (isJarURL(rootDirUrl) /* normal url-class-loader use case */
                        || isJarFileURL(rootDirUrl) /* system class loader ? */) {
                        return this.findPathMatchingJarResources(rootDirUrl, locationPattern);
                    } else {
                        // FIXME
                        return this.findPathMatchingFileSystemResources(rootDirUrl, locationPattern);
                    }
                });
    }

    private Stream<URL> findPathMatchingJarResources(URL jarRootUrl, String locationPattern) {

        if (log.isTraceEnabled()) {
            log.trace("-- looking for '" + locationPattern + "' in " + jarRootUrl.toString());
        }

        URLConnection con = null;
        JarFile jarFile = null;
        try {
            con = jarRootUrl.openConnection();

            String jarFileUrl;
            String rootEntryPath;

            if (con instanceof JarURLConnection) {
                // Should usually be the case for traditional JAR files.
                JarURLConnection jarCon = (JarURLConnection) con;
                jarFile = jarCon.getJarFile();
                jarFileUrl = jarCon.getJarFileURL().toExternalForm();
                JarEntry jarEntry = jarCon.getJarEntry();
                rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
            } else {
                // No JarURLConnection when using this from Gradle
                jarFileUrl = jarRootUrl.getFile();
                jarFile = new JarFile(jarFileUrl);

                if (jarRootUrl.getProtocol().equals("file")) {
                    // reconfigure jarRootUrl to be a real jar:-url
                    jarRootUrl = new URL("jar:"+jarRootUrl.toString()+"!/");
                }
                rootEntryPath = "";
            }

            if (log.isTraceEnabled()) {
                log.trace("Looking for matching resources in jar file [" + jarFileUrl + "]");
            }

            if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
                rootEntryPath = rootEntryPath + "/";
            }

            Set<URL> result = new LinkedHashSet<>(8);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryPath = entry.getName();

                // crazy (version?) feature in .jar files:
                // the root is not always "/", but can be a subdirectory
                if (entryPath.startsWith(rootEntryPath)) {
                    String relativePath = entryPath.substring(rootEntryPath.length());
                    if (this.pathMatcher.matches(locationPattern, relativePath)) {
                        if (relativePath.startsWith("/")) {
                            // rootEntryPath has a trailing '/'
                            relativePath = relativePath.substring(1);
                        }
                        URL url = new URL(jarRootUrl, rootEntryPath + relativePath);
                        result.add(url);
                    }
                }
            }

            return result.stream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {

            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private Stream<URL> findPathMatchingFileSystemResources(URL rootDirUrl, String pattern) {

        if (!"file".equals(rootDirUrl.getProtocol())) {
            return Stream.empty();
        }

        try {
            Path rootDir = Paths.get(new URI(rootDirUrl.toString())).toAbsolutePath();

            // calculate the 'full' pattern, including the root-dir
            String fullPattern = StringUtils.replace(rootDir.toString(), File.separator, "/");
            if (!pattern.startsWith("/")) {
                fullPattern += "/";
            }
            fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");

            String finalFullPattern = fullPattern;
            return Files.walk(rootDir)
                    .filter(file -> {
                        // normalize path separator for path matcher
                        String path = StringUtils.replace(file.toAbsolutePath().toString(), File.separator, "/");
                        return this.pathMatcher.matches(finalFullPattern, path);
                    })
                    .map(file -> {
                        URL url = null;
                        try {
                            url = file.toUri().toURL();
                        } catch (MalformedURLException e) {
                            log.warn("Found a match " + file.toAbsolutePath().toString() + " but cannot get URL");
                        }
                        return Optional.ofNullable(url);
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get);
        } catch (URISyntaxException e) {
            log.warn("Invalid URI: ", e);
            return Stream.empty();
        } catch (IOException e) {
            log.warn("IOException: ", e);
            return Stream.empty();
        }
    }

    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return ("jar".equals(protocol) || "war".equals(protocol) || "zip".equals(protocol));
    }

    public static boolean isJarFileURL(URL url) {
        return ("file".equals(url.getProtocol()) &&
                url.getPath().toLowerCase().endsWith(".jar"));
    }

    private static <T> Stream<T> asStream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<>() {
                            public T next() {
                                return e.nextElement();
                            }

                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }

                            public void forEachRemaining(Consumer<? super T> action) {
                                while (e.hasMoreElements()) {
                                    action.accept(e.nextElement());
                                }
                            }
                        },
                        Spliterator.ORDERED), false);
    }

    private static class StringUtils {


        public static String replace(String inString, String oldPattern, String newPattern) {
            if (inString == null || inString.isEmpty()
                    || oldPattern == null || oldPattern.isEmpty()
                    || newPattern == null) {
                return inString;
            }

            int index = inString.indexOf(oldPattern);
            if (index == -1) {
                // no occurrence -> can return input as-is
                return inString;
            }

            int capacity = inString.length();
            if (newPattern.length() > oldPattern.length()) {
                capacity += 16;
            }
            StringBuilder sb = new StringBuilder(capacity);

            int pos = 0;  // our position in the old string
            int patLen = oldPattern.length();
            while (index >= 0) {
                sb.append(inString.substring(pos, index));
                sb.append(newPattern);
                pos = index + patLen;
                index = inString.indexOf(oldPattern, pos);
            }

            // append any characters to the right of a match
            sb.append(inString.substring(pos));
            return sb.toString();
        }
    }
}
