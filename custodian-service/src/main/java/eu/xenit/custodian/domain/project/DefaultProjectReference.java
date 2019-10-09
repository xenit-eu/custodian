package eu.xenit.custodian.domain.project;

import java.net.URI;
import org.springframework.util.Assert;

public class DefaultProjectReference implements ProjectReference {

    private final URI uri;

    DefaultProjectReference(URI uri) {
        Assert.notNull(uri, "uri must not be null");
        this.uri = uri;
    }

    public URI getUri() {
        return this.uri;
    }

}
