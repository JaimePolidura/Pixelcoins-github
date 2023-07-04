package es.serversurvival._shared.cache;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Cache<K, V> {
    V put(K key, V value);
    void remove(K key);
    void remove(Predicate<V> condition);
    Optional<V> find(K key);
    List<V> all();
    void clear();
    int size();
    Optional<V> findValueIf(Predicate<V> valueCondition);
    List<V> findValuesIf(Predicate<V> valueCondition);
}
