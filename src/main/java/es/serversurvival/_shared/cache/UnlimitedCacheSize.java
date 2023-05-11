package es.serversurvival._shared.cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public final class UnlimitedCacheSize<K, V> implements Cache<K, V>{
    private final Map<K, V> cacheItems;

    public UnlimitedCacheSize(){
        this.cacheItems = new ConcurrentHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        this.cacheItems.put(key, value);
    }

    @Override
    public void remove(K key) {
        this.cacheItems.remove(key);
    }

    @Override
    public void remove(Predicate<V> condition) {
        this.cacheItems.entrySet().stream()
                .filter(entryset -> condition.test(entryset.getValue()))
                .findFirst()
                .ifPresent(keyset -> this.cacheItems.remove(keyset.getKey()));
    }

    @Override
    public Optional<V> find(K key) {
        return Optional.ofNullable(this.cacheItems.get(key));
    }

    @Override
    public List<V> all() {
        return this.cacheItems.values().stream().toList();
    }

    @Override
    public void clear() {
        this.cacheItems.clear();
    }

    @Override
    public int size() {
        return this.cacheItems.size();
    }

    @Override
    public Optional<V> findValueIf(Predicate<V> valueCondition) {
        return this.cacheItems.values().stream()
                .filter(valueCondition)
                .findFirst();
    }

    @Override
    public List<V> findValuesIf(Predicate<V> valueCondition) {
        return this.cacheItems.values().stream()
                .filter(valueCondition)
                .toList();
    }
}
