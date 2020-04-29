package eu.xenit.custodian.app.github.adapters.githubclient.http;

import com.budjb.httprequests.HttpEntity;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

public class JacksonEntityReader implements HttpEntityReader {

    private final Logger logger = LoggerFactory.getLogger(JacksonEntityReader.class);

    /**
     * Jackson object mapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor.
     *
     * @param objectMapper Jackson object mapper.
     */
    public JacksonEntityReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRead(Class<?> clazz, String contentType) {

        JavaType javaType = getJavaType(clazz, null);
        AtomicReference<Throwable> causeRef = new AtomicReference<>();
        if (this.objectMapper.canDeserialize(javaType, causeRef)) {
            return true;
        }

        logWarningIfNecessary(javaType, causeRef.get());

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T read(Class<? extends T> clazz, HttpEntity entity) throws IOException {
        JavaType javaType = getJavaType(clazz, null);
        return (T) this.objectMapper.readValue(entity.getInputStream(), javaType);
    }

    /**
     * Return the Jackson {@link JavaType} for the specified type and context class.
     * @param type the generic type to return the Jackson JavaType for
     * @param contextClass a context class for the target type, for example a class
     * in which the target type appears in a method signature (can be {@code null})
     * @return the Jackson JavaType
     */
    protected JavaType getJavaType(Type type, @Nullable Class<?> contextClass) {
        // currently contextClass is ignored: if we run into blocking issues with generic types,
        // we should look at SpringFramework's `GenericTypeResolver.resolveType(type, contextClass)`
        TypeFactory typeFactory = this.objectMapper.getTypeFactory();
        return typeFactory.constructType(type);
    }

    /**
     * Determine whether to log the given exception coming from a
     * {@link ObjectMapper#canDeserialize} / {@link ObjectMapper#canSerialize} check.
     * @param type the class that Jackson tested for (de-)serializability
     * @param cause the Jackson-thrown exception to evaluate
     * (typically a {@link JsonMappingException})
     * @since 4.3
     */
    protected void logWarningIfNecessary(Type type, @Nullable Throwable cause) {
        if (cause == null) {
            return;
        }

        // Do not log warning for serializer not found (note: different message wording on Jackson 2.9)
       // boolean debugLevel = (cause instanceof JsonMappingException && cause.getMessage().startsWith("Cannot find"));

        //if (debugLevel ? logger.isDebugEnabled() : logger.isWarnEnabled()) {
            String msg = "Failed to evaluate Jackson " + (type instanceof JavaType ? "de" : "") +
                    "serialization for type [" + type + "]";
//            if (debugLevel) {
//                logger.debug(msg, cause);
//            }
//            else if (logger.isDebugEnabled()) {
//                logger.warn(msg, cause);
//            }
//            else {
                logger.warn(msg + ": " + cause);
//            }
//        }
    }

}
