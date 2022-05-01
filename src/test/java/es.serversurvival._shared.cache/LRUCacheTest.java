package es.serversurvival._shared.cache;

import org.junit.Before;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

public final class LRUCacheTest {
    private static final int MAX_CAPACITY = 3;
    private final LRUCache<String, Integer> lruCache;

    public LRUCacheTest(){
        this.lruCache = new LRUCacheTestImpl();
    }

    @BeforeTest
    public void beforeTest(){
        this.lruCache.clear();
    }

    @Test
    public void shouldReplace(){
        this.lruCache.add("item", 1);
        assertTrue(this.lruCache.get("item").isPresent());
        assertEquals(1, this.lruCache.get("item").get(), 0);

        this.lruCache.add("item", 2);
        assertTrue(this.lruCache.get("item").isPresent());
        assertEquals(2, this.lruCache.get("item").get(), 0);
        assertTrue(this.lruCache.get("item").get() != 1);
    }

    @Test
    public void shouldSaveAndGet(){
        for (int i = 0; i < MAX_CAPACITY; i++) {
            this.lruCache.add(String.valueOf(i), 1);
            assertTrue(this.lruCache.get(String.valueOf(i)).isPresent());
        }

        this.lruCache.add("extra", 4);
        assertTrue(this.lruCache.get("extra").isPresent());

        assertFalse(this.lruCache.get("0").isPresent());
        assertTrue(this.lruCache.get("1").isPresent());

        this.lruCache.get("1");
        this.lruCache.add("extra2", 2);
        assertTrue(this.lruCache.get("1").isPresent());
    }

    private static class LRUCacheTestImpl extends LRUCache<String, Integer> {
        @Override
        public int maxItemsCapacity() {
            return MAX_CAPACITY;
        }
    }
}
