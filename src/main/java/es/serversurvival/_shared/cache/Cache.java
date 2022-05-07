package es.serversurvival._shared.cache;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Cache<K, V> {
    int maxItemsCapacity();

    void put(K key, V value);

    void delete(K key);

    void delete(Predicate<V> condition);

    Optional<V> find(K key);

    List<V> all();

    void clear();

    int size();

    Optional<V> findValue(Predicate<V> valueCondition);

    List<V> findValues(Predicate<V> valueCondition);


    default boolean isFull() {
        return this.size() == maxItemsCapacity();
    }

    default boolean isNotFull(){
        return !this.isFull();
    }
}
