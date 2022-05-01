package es.serversurvival._shared.cache;

import java.util.Optional;

public interface Cache<K, V> {
    int maxItemsCapacity();

    void add(K key, V value);

    Optional<V> get(K key);

    void clear();
}
