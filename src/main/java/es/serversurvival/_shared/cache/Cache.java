package es.serversurvival._shared.cache;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Cache<K, V> {
    int maxItemsCapacity();
    void put(K key, V value);
    void removeValueIf(K key);
    void removeValueIf(Predicate<V> condition);
    Optional<V> find(K key);
    List<V> all();
    void clear();
    int size();
    Optional<V> findValueIf(Predicate<V> valueCondition);
    List<V> findValuesIf(Predicate<V> valueCondition);

    default boolean isFull() {
        return this.size() == maxItemsCapacity();
    }

    default boolean isNotFull(){
        return !this.isFull();
    }
}
