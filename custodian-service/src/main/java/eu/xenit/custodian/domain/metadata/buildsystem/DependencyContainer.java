package eu.xenit.custodian.domain.metadata.buildsystem;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

public class DependencyContainer<I extends DependencyIdentifier, D extends Dependency<I>> {

    private final Map<I, D> dependencies;

    private final Function<String, I> notationParser;
    private final Function<D, I> keyFunction;

    private final BinaryOperator<D> mergeFunction;

    protected DependencyContainer(Function<String, I> notationParser) {
        this(new LinkedHashMap<>(), notationParser, Dependency::getId);
    }

    protected DependencyContainer(Map<I, D> dependencies, Function<String, I> notationParser, Function<D, I> keyFunction)
    {
        this(dependencies, notationParser, keyFunction, null);
    }

    protected DependencyContainer(Map<I, D> dependencies, Function<String, I> notationParser, Function<D, I> keyFunction, BinaryOperator<D> mergeFunction) {
        Objects.requireNonNull(dependencies, "dependencies must not be null");
        Objects.requireNonNull(notationParser, "notationParser must not be null");
        Objects.requireNonNull(keyFunction, "keyFunction must not be null");

        this.dependencies = dependencies;
        this.notationParser = notationParser;
        this.keyFunction = keyFunction;
        this.mergeFunction = mergeFunction != null ? mergeFunction : (d1, d2) -> {
            throw new IllegalStateException("Merging dependencies is not supported");
        };
    }

    public D get(I id) {
        return this.dependencies.get(id);
    }

    public D get(String notation) {
        return this.get(this.notationParser.apply(notation));
    }

    public void add(D dependency) {
        I id = this.keyFunction.apply(dependency);
        this.add(id, dependency);
    }

    protected synchronized void add(I id, D dependency) {
        D current = this.dependencies.putIfAbsent(id, dependency);
        if (current != null) {
            D merged = this.mergeFunction.apply(current, dependency);
            this.dependencies.put(id, merged);
        }
    }

    public void addAll(Collection<D> collection)
    {
        Objects.requireNonNull(collection, "collection must not be null");
        collection.forEach(dependency -> {
            Objects.requireNonNull(dependency, "dependency in collection must not be null");
            this.add(dependency);
        });
    }

    public int size() {
        return this.dependencies.size();
    }

    public Stream<D> stream() {
        return this.dependencies.values().stream();
    }

    public Stream<I> ids() {
        return this.dependencies.keySet().stream();
    }
}
