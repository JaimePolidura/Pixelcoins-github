package es.serversurvival._shared.cache;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class LRUCache<K, V> implements LimitedCache<K, V> {
    //Last index: most not recently used
    //first index: most recently used
    private final LinkedList<CacheItem<K, V>> items;
    private final int maxCapacity;
    private final Lock lock;

    public LRUCache(int maxCapacity){
        this.lock = new ReentrantLock();
        this.items = new LinkedList<>();
        this.maxCapacity = maxCapacity;
    }

    @Override
    public V put(K key, V value) {
        this.lock.lock();

        removeIfExists(key, value);
        addFirst(key, value);

        if(hasCacheExceedCapacity()) {
            removeLast();
        }

        this.lock.unlock();

        return value;
    }

    @Override
    public void remove(K key) {
        this.lock.lock();
        this.items.removeIf(cacheItem -> cacheItem.getKey().equals(key));
        this.lock.unlock();
    }

    @Override
    public void remove(Predicate<V> condition) {
        this.lock.lock();
        this.items.removeIf(cacheItem -> condition.test(cacheItem.getValue()));
        this.lock.unlock();
    }

    @Override
    public Optional<V> find(K key) {
        int index = 0;

        for (CacheItem<K, V> actualCacheItem : new LinkedList<>(this.items)) {
            if(actualCacheItem.key.equals(key)){
                this.moveItemToFirstPosition(index, actualCacheItem);

                return Optional.of(actualCacheItem.value);
            }

            index++;
        }

        this.lock.unlock();

        return Optional.empty();
    }

    @Override
    public List<V> all() {
        return this.items.stream()
                .map(CacheItem::getValue)
                .toList();
    }

    @Override
    public Optional<V> findValueIf(Predicate<V> valueCondition) {
        int index = 0;

        for (CacheItem<K, V> actualCacheItem : new LinkedList<>(this.items)) {
            if(valueCondition.test(actualCacheItem.value)){
                this.moveItemToFirstPosition(index, actualCacheItem);

                return Optional.of(actualCacheItem.value);
            }

            index++;
        }

        return Optional.empty();

    }

    @Override
    public List<V> findValuesIf(Predicate<V> valueCondition) {
        List<V> valuesFound = new LinkedList<>();
        int index = 0;

        //Create copy to ensure not concurrent exception
        for (CacheItem<K, V> actualCacheItem : new LinkedList<>(this.items)) {
            if(valueCondition.test(actualCacheItem.value)){
                this.moveItemToFirstPosition(index, actualCacheItem);

                valuesFound.add(actualCacheItem.value);
            }

            index++;
        }

        return valuesFound;
    }

    @Override
    public int maxItemsCapacity() {
        return this.maxCapacity;
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public int size() {
        return this.items.size();
    }

    private void removeIfExists(K key, V value){
        this.items.remove(new CacheItem<>(key, value));
    }

    private void moveItemToFirstPosition(int index, CacheItem<K, V> item){
        this.lock.lock();
        this.items.remove(index);
        this.items.addFirst(item);
        this.lock.unlock();
    }

    private void addFirst(K key, V value){
        this.items.add(0, new CacheItem<>(key, value));
    }

    private void removeLast(){
        this.items.removeLast();
    }

    private boolean hasCacheExceedCapacity(){
        return this.items.size() > maxItemsCapacity();
    }

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    private static class CacheItem<K, V>{
        @Getter private final K key;
        @Getter private final V value;
    }
}
