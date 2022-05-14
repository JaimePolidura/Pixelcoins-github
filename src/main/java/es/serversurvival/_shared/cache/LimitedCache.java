package es.serversurvival._shared.cache;

public interface LimitedCache<K, V> extends Cache<K, V>{
    int maxItemsCapacity();
    int size();

    default boolean isFull() {
        return this.size() == maxItemsCapacity();
    }

    default boolean isNotFull(){
        return !this.isFull();
    }
}
