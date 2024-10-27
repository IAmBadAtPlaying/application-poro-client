import com.iambadatplaying.ressourceServer.LRUCacheMap;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestLRUMap {
    @Test
    public void testLRURemoveOldest() {
        LRUCacheMap<Integer, Integer> lru = new LRUCacheMap<>(3);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.put(3, 3);
        lru.put(4, 4);
        assertFalse(lru.containsKey(1));
        assertEquals(lru.size(),3);
    }

    @Test
    public void testLRURefreshesEntries() {
        final int capacity = 3;
        LRUCacheMap<Integer, Integer> lru = new LRUCacheMap<>(capacity);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.put(3, 3);
        //Refresh 1
        lru.get(1);
        lru.put(4, 4);
        assertEquals(capacity, lru.size());
        assertNull(lru.get(2));
    }
}
