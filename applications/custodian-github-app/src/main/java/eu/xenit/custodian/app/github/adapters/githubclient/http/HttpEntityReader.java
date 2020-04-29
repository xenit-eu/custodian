package eu.xenit.custodian.app.github.adapters.githubclient.http;

import com.budjb.httprequests.HttpEntity;
import java.io.IOException;
import java.util.List;

public interface HttpEntityReader {

    /**
     * Indicates whether the given class can be read by this converter.
     * @param clazz the class to test for readability
     * @param contentType the {@code Content-Type} header (can be {@code null} if not specified)
     * @return {@code true} if readable; {@code false} otherwise
     */
    boolean canRead(Class<?> clazz, String contentType);

    /**
     * Read an object of the given type from the given input message, and returns it.
     * @param clazz the type of object to return. This type must have previously been passed to the
     * {@link #canRead canRead} method of this interface, which must have returned {@code true}.
     * @param entity the HTTP entity to read from
     * @return the converted object
     * @throws IOException in case of I/O errors
     */
    <T> T read(Class<? extends T> clazz, HttpEntity entity) throws IOException;
}
