package eu.xenit.custodian.sentinel.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class ItemContainer<I, V> {

    private final Map<I, V> items;

    private final Function<V, I> keyFunction;

    protected ItemContainer(Map<I, V> items, Function<V, I> keyFunction) {
        this.items = items;
        this.keyFunction = keyFunction;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public boolean has(I id) {
        return this.items.containsKey(id);
    }

    public Set<I> ids() {
        return this.items.keySet();
    }

    @JsonValue
    public Collection<V> items() {
        return this.items.values();
    }

    public V get(I id) {
        return this.items.get(id);
    }

//    public void add(V item) {
//
//        I key = this.keyFunction.apply(item);
//        if (key == null) {
//            throw new IllegalArgumentException("No valid key for value: "+item);
//        }
//
//        this.items.put(key, item);
//    }

    /**
     * Register the specified {@code item} with the specified {@code id}.
     * @param id the id of the item
     * @param item the item to register
     */
    public void add(I id, V item) {
        this.items.put(id, item);
    }

    public boolean remove(I id) {
        return this.items.remove(id) != null;
    }
}
