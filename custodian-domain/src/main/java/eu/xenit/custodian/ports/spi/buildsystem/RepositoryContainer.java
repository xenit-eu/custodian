package eu.xenit.custodian.ports.spi.buildsystem;

import eu.xenit.custodian.domain.buildsystem.BuildItemContainer;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;

public class RepositoryContainer<TRepository extends Repository> extends BuildItemContainer<String, TRepository> {

    protected RepositoryContainer() {
        super(new LinkedHashMap<>());
    }

    public void add(TRepository repository) {
        super.add(repository.getId(), repository);
    }

    public void addAll(Collection<TRepository> collection)
    {
        Objects.requireNonNull(collection, "collection must not be null");
        collection.forEach(repository -> {
            Objects.requireNonNull(repository, "item in collection must not be null");
            this.add(repository);
        });
    }
}
