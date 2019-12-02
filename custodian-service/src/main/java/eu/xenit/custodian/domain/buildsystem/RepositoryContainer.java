package eu.xenit.custodian.domain.buildsystem;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;

public class RepositoryContainer<R extends Repository> extends BuildItemContainer<String, R> {

    protected RepositoryContainer() {
        super(new LinkedHashMap<>());
    }

    public void add(R repository) {
        super.add(repository.getId(), repository);
    }

    public void addAll(Collection<R> collection)
    {
        Objects.requireNonNull(collection, "collection must not be null");
        collection.forEach(repository -> {
            Objects.requireNonNull(repository, "item in collection must not be null");
            this.add(repository);
        });
    }
}
