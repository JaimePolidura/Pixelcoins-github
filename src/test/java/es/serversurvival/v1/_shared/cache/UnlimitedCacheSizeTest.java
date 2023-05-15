package es.serversurvival.v1._shared.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public final class UnlimitedCacheSizeTest {
    private final UnlimitedCacheSize<String, Integer> cache;

    public UnlimitedCacheSizeTest(){
        this.cache = new UnlimitedCacheSize<>();
    }

    @BeforeEach
    public void beforeTest(){
        this.cache.clear();
    }

    @Test
    public void shouldReplace(){
        this.cache.put("item", 1);
        assertTrue(this.cache.find("item").isPresent());
        assertEquals(1, this.cache.find("item").get(), 0);

        this.cache.put("item", 2);
        assertTrue(this.cache.find("item").isPresent());
        assertEquals(2, this.cache.find("item").get(), 0);
        assertTrue(this.cache.find("item").get() != 1);
    }
}
