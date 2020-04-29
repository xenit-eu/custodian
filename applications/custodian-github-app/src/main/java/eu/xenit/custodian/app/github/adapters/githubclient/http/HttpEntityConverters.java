package eu.xenit.custodian.app.github.adapters.githubclient.http;

import com.budjb.httprequests.ConvertingHttpEntity;
import com.budjb.httprequests.HttpEntity;
import com.budjb.httprequests.converter.EntityConverterManager;
import com.budjb.httprequests.exception.UnsupportedConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpEntityConverters extends EntityConverterManager {

    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(EntityConverterManager.class);

    /**
     * List of registered entity converters.
     */
    private final List<HttpEntityReader> readers;


    public HttpEntityConverters(List<HttpEntityReader> readers) {
        super(Collections.emptyList());

//        Comparator<EntityConverter> comparator = (o1, o2) -> {
//            int l = (o1 instanceof Ordered) ? ((Ordered) o1).getOrder() : 0;
//            int r = (o2 instanceof Ordered) ? ((Ordered) o2).getOrder() : 0;
//
//            return Integer.compare(r, l);
//        };
//        converters = entityConverters.stream().sorted(comparator).collect(Collectors.toList());
        this.readers = Collections.unmodifiableList(readers);
    }

    /**
     * Attempts to convert the given entity into an {@link InputStream}.
     *
     * @param entity Entity object to convert.
     * @return Converted entity as an InputStream.
     * @throws UnsupportedConversionException when there are no entity writers that support the object type.
     */
    public HttpEntity write(Object entity) throws UnsupportedConversionException {
        return write(entity, null);
    }

    /**
     * Attempts to convert the given entity into an {@link InputStream} with the given content type.
     *
     * @param entity      Entity object to convert.
     * @param contentType Content type of the entity.
     * @return Converted entity as an InputStream.
     * @throws UnsupportedConversionException when there are no entity writers that support the object type.
     */
    public HttpEntity write(Object entity, String contentType) throws UnsupportedConversionException {
        return write(entity, contentType, null);
    }

    /**
     * Attempts to convert the given entity into an {@link InputStream}.
     *
     * @param entity Converting HTTP entity.
     * @return Converted entity as an InputStream.
     * @throws UnsupportedConversionException when there are no entity writers that support the object type.
     */
    public HttpEntity write(ConvertingHttpEntity entity) throws UnsupportedConversionException {
        return write(entity.getObject(), entity.getContentType(), entity.getCharSet());
    }

    /**
     * Attempts to convert the given entity into an {@link InputStream} with the given content type and character set.
     *
     * @param entity       Entity object to convert.
     * @param contentType  Content type of the entity.
     * @param characterSet Character set of the entity.
     * @return Converted entity as an InputStream.
     * @throws UnsupportedConversionException when there are no entity writers that support the object type.
     */
    public HttpEntity write(Object entity, String contentType, String characterSet) throws UnsupportedConversionException {
        return super.write(entity, contentType, characterSet);
    }

    /**
     * Reads an object from the given entity {@link InputStream}.
     *
     * @param type   Object type to attempt conversion to.
     * @param entity Entity input stream.
     * @param <T>    Generic type of the method call.
     * @return The converted object.
     * @throws UnsupportedConversionException when there are no entity writers that support the object type.
     * @throws IOException                    When an IO exception occurs.
     */
    @SuppressWarnings("unchecked")
    public <T> T read(Class<?> type, HttpEntity entity) throws UnsupportedConversionException, IOException {
        for (HttpEntityReader reader : this.readers) {
            if (reader.canRead(type, entity.getContentType())) {
                return (T) reader.read(type, entity);
            }
        }

        throw new UnsupportedConversionException(type);
    }
}
