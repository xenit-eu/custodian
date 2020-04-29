package eu.xenit.custodian.domain.project;

import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import java.net.URI;
import org.springframework.util.Assert;

public class DefaultSourceRepositoryReference implements SourceRepositoryReference {

    private final URI uri;

    DefaultSourceRepositoryReference(URI uri) {
        Assert.notNull(uri, "uri must not be null");
        this.uri = uri;
    }

    public URI getUri() {
        return this.uri;
    }

}
