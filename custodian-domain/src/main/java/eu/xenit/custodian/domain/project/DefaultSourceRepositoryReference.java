package eu.xenit.custodian.domain.project;

import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.util.Arguments;
import java.net.URI;

public class DefaultSourceRepositoryReference implements SourceRepositoryReference {

    private final URI uri;

    DefaultSourceRepositoryReference(URI uri) {
        Arguments.notNull(uri, "uri");
        this.uri = uri;
    }

    public URI getUri() {
        return this.uri;
    }

}
