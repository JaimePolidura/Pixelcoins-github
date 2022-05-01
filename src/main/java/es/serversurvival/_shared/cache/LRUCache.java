package es.serversurvival._shared.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Optional;

public abstract class LRUCache<K, V> implements Cache<K, V>{
    //Last index: most not recently used
    //first index: most recently used
    private final LinkedList<CacheItem<K, V>> items;

    public LRUCache(){
        this.items = new LinkedList<>();
    }

    @Override
    public void add(K key, V value) {
        removeIfExists(key, value);
        addFirst(key, value);

        if(hasCacheExceedCapacity()) {
            removeLast();
        }
    }

    @Override
    public Optional<V> get(K key) {
        int index = 0;

        for (CacheItem<K, V> actualCacheItem : this.items) {
            if(actualCacheItem.key.equals(key)){
                this.moveItemToFirstPosition(index, actualCacheItem);

                return Optional.of(actualCacheItem.value);
            }
        }

        return Optional.empty();
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    private void removeIfExists(K key, V value){
        this.items.remove(new CacheItem<>(key, value));
    }

    private void moveItemToFirstPosition(int index, CacheItem<K, V> item){
        this.items.remove(index);
        this.items.addFirst(item);
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
    private static class CacheItem<K, V>{
        @Getter private final K key;
        @Getter private final V value;
    }
}
