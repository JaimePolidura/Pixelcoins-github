package es.serversurvival._shared.cache;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class LRUCacheTest {
    private static final int MAX_CAPACITY = 3;
    private final LRUCache<String, Integer> lruCache;

    public LRUCacheTest(){
        this.lruCache = new LRUCache<>(MAX_CAPACITY);
    }

    @BeforeEach
    public void beforeTest(){
        this.lruCache.clear();
    }

    @Test
    public void shouldReplace(){
        this.lruCache.put("item", 1);
        Assertions.assertTrue(this.lruCache.find("item").isPresent());
        Assertions.assertEquals(1, this.lruCache.find("item").get(), 0);

        this.lruCache.put("item", 2);
        Assertions.assertTrue(this.lruCache.find("item").isPresent());
        Assertions.assertEquals(2, this.lruCache.find("item").get(), 0);
        Assertions.assertTrue(this.lruCache.find("item").get() != 1);
    }

    @Test
    public void shouldSaveAndGet(){
        for (int i = 0; i < MAX_CAPACITY; i++) {
            this.lruCache.put(String.valueOf(i), 1);
            Assertions.assertTrue(this.lruCache.find(String.valueOf(i)).isPresent());
        }

        this.lruCache.put("extra", 4);
        Assertions.assertTrue(this.lruCache.find("extra").isPresent());

        Assertions.assertFalse(this.lruCache.find("0").isPresent());
        Assertions.assertTrue(this.lruCache.find("1").isPresent());

        this.lruCache.find("1");
        this.lruCache.put("extra2", 2);
        Assertions.assertTrue(this.lruCache.find("1").isPresent());
    }
}
