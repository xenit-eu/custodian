package eu.xenit.custodian.ports.spi.buildsystem;

import eu.xenit.custodian.util.Arguments;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryContainer<TRepository extends Repository> {

    private final Map<String, TRepository> repositories = new LinkedHashMap<>();

    protected RepositoryContainer() {

    }

    protected RepositoryContainer(Stream<TRepository> repositories) {
        Arguments.notNull(repositories, "repositories");
        repositories.forEach(this::add);
    }


    /**
     * Specify if this repository container is empty.
     * @return {@code true} if no repository is registered
     */
    public boolean isEmpty() {
        return this.repositories.isEmpty();
    }

    /**
     * Specify if this repository container has an item with the specified url.
     *
     * @param url the url of a repository
     * @return {@code true} if a repository with the specified {@code url} is registered
     */
    public boolean hasRepository(String url) {
        return this.repositories.containsKey(url);
    }

    /**
     * Return a {@link Stream} of registered repositories.
     * @return a stream of repository urls
     */
    public Stream<String> urls() {
        return this.repositories.keySet().stream();
    }

    /**
     * Return a {@link Stream} of registered repositories.
     * @return a stream of items
     */
    public Stream<TRepository> items() {
        return this.repositories.values().stream();
    }

    /**
     * Return the repository with the specified {@code url} or {@code null} if no such
     * repository exists.
     *
     * @param url the url of a repository
     * @return the {@code optional} of repository or {@code Optional.empty()}
     */
    public Optional<TRepository> get(String url) {
        return Optional.ofNullable(this.repositories.get(url));
    }

    /**
     * Register the specified {@code repository}.
     *
     * @param repository the repository to register
     */
    protected void add(TRepository repository) {
        this.repositories.put(repository.getUrl(), repository);
    }
}
