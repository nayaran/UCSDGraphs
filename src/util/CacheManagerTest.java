package util;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class CacheManagerTest {
	private static CacheManager cacheManager;
	@Before
	public void setup() {
		cacheManager = new CacheManager();
	}
	@Test
	public void testPutInCacheAndGetFromCache() {
		Integer number = new Integer(8);
		CachedObject cachedNumber = new CachedObject(number, number.hashCode(), 0);

		cacheManager.putInCache(cachedNumber);
		CachedObject retrievedNumber = cacheManager.getFromCache(number.hashCode());
		assertEquals("testPutInCacheAndGetFromCache - Testing basic functionality of cache", cachedNumber, retrievedNumber);
	}
	
	@Test
	public void testGetFromCacheForExpiredObject() throws InterruptedException {
		Integer number = new Integer(9);
		CachedObject cachedNumber = new CachedObject(number, number.hashCode(), 10);

		cacheManager.putInCache(cachedNumber);
		Thread.sleep(12 * 1000);
		
		CachedObject retrievedNumber = cacheManager.getFromCache(number.hashCode());
		assertNull("testGetFromCacheForExpiredObject - Testing expiry of object in cache", retrievedNumber);
	}
	
	@Test
	public void testGetFromCacheForAliveObjects() throws InterruptedException {
			Integer number = new Integer(9);
			CachedObject cachedNumber = new CachedObject(number, number.hashCode(), 10);

			cacheManager.putInCache(cachedNumber);
			Thread.sleep(5 * 1000);
			
			CachedObject retrievedNumber = cacheManager.getFromCache(number.hashCode());
			assertNotNull("testGetFromCacheForAliveObjects - Testing aliveness of object in cache", retrievedNumber);
	}
}
