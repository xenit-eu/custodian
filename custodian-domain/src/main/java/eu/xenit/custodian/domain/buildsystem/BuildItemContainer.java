package eu.xenit.custodian.domain.buildsystem;

import java.util.Map;
import java.util.stream.Stream;

public class BuildItemContainer<I, V> {

    private final Map<I, V> items;

    protected BuildItemContainer(Map<I, V> items) {
        this.items = items;
    }

    /**
     * Specify if this container is empty.
     * @return {@code true} if no item is registered
     */
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    /**
     * Specify if this container has an item with the specified id.
     * @param id the id of an item
     * @return {@code true} if an item with the specified {@code id} is registered
     */
    public boolean has(I id) {
        return this.items.containsKey(id);
    }

    /**
     * Return a {@link Stream} of registered identifiers.
     * @return a stream of ids
     */
    public Stream<I> ids() {
        return this.items.keySet().stream();
    }

    /**
     * Return a {@link Stream} of registered items.
     * @return a stream of items
     */
    public Stream<V> items() {
        return this.items.values().stream();
    }

    /**
     * Return the item with the specified {@code id} or {@code null} if no such item
     * exists.
     * @param id the id of an item
     * @return the item or {@code null}
     */
    public V get(I id) {
        return this.items.get(id);
    }

    /**
     * Register the specified {@code item} with the specified {@code id}.
     * @param id the id of the item
     * @param item the item to register
     */
    public void add(I id, V item) {
        this.items.put(id, item);
    }

    /**
     * Remove the item with the specified {@code id}.
     * @param id the id of the item to remove
     * @return {@code true} if such an item was registered, {@code false} otherwise
     */
    public boolean remove(I id) {
        return this.items.remove(id) != null;
    }

}
