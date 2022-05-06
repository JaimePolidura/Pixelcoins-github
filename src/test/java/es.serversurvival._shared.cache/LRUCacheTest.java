package es.serversurvival._shared.cache;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

public final class LRUCacheTest {
    private static final int MAX_CAPACITY = 3;
    private final LRUCache<String, Integer> lruCache;

    public LRUCacheTest(){
        this.lruCache = new LRUCache<>(MAX_CAPACITY);
    }

    @BeforeTest
    public void beforeTest(){
        this.lruCache.clear();
    }

    @Test
    public void shouldReplace(){
        this.lruCache.add("item", 1);
        assertTrue(this.lruCache.find("item").isPresent());
        assertEquals(1, this.lruCache.find("item").get(), 0);

        this.lruCache.add("item", 2);
        assertTrue(this.lruCache.find("item").isPresent());
        assertEquals(2, this.lruCache.find("item").get(), 0);
        assertTrue(this.lruCache.find("item").get() != 1);
    }

    @Test
    public void shouldSaveAndGet(){
        for (int i = 0; i < MAX_CAPACITY; i++) {
            this.lruCache.add(String.valueOf(i), 1);
            assertTrue(this.lruCache.find(String.valueOf(i)).isPresent());
        }

        this.lruCache.add("extra", 4);
        assertTrue(this.lruCache.find("extra").isPresent());

        assertFalse(this.lruCache.find("0").isPresent());
        assertTrue(this.lruCache.find("1").isPresent());

        this.lruCache.find("1");
        this.lruCache.add("extra2", 2);
        assertTrue(this.lruCache.find("1").isPresent());
    }
}
