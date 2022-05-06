package es.serversurvival._shared.cache;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Cache<K, V> {
    int maxItemsCapacity();

    void add(K key, V value);

    void delete(K key);

    Optional<V> find(K key);

    List<V> all();

    void clear();

    boolean isFull();

    Optional<V> findValue(Predicate<V> valueCondition);

    List<V> findValues(Predicate<V> valueCondition);

    default boolean notFull(){
        return !this.isFull();
    }
}
