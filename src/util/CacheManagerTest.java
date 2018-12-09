package util;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class CacheManagerTest {
	private static CacheManager cacheManager;
	//@Before
	public void setup() {
		cacheManager = new CacheManager();
	}
	//@Test
	public void testPutInCacheAndGetFromCacheForInteger() {
		Integer number = new Integer(8);
		cacheManager.put(number, 2);
		Integer retrievedNumber = (Integer) cacheManager.get(number.hashCode());
		assertEquals("testPutInCacheAndGetFromCache - Testing basic functionality of cache", number, retrievedNumber);
	}
	//@Test
	public void testPutInCacheAndGetFromCacheForString() {
		String name = new String("anurag");
		cacheManager.put(name, 2);
		String retrievedName = (String) cacheManager.get(name.hashCode());
		assertEquals("testPutInCacheAndGetFromCache - Testing basic functionality of cache", name, retrievedName);
	}
	
	//@Test
	public void testGetFromCacheForExpiredObject() throws InterruptedException {
		Integer number = new Integer(9);
		cacheManager.put(number, 2);
		Thread.sleep(3 * 1000);
		
		Integer retrievedNumber = (Integer) cacheManager.get(number.hashCode());
		assertNull("testGetFromCacheForExpiredObject - Testing expiry of object in cache", retrievedNumber);
	}
	
	//@Test
	public void testGetFromCacheForAliveObjects() throws InterruptedException {
			Integer number = new Integer(9);

			cacheManager.put(number, 2);
			Thread.sleep(1 * 1000);
			
			Integer retrievedNumber = (Integer) cacheManager.get(number.hashCode());
			assertNotNull("testGetFromCacheForAliveObjects - Testing aliveness of object in cache", retrievedNumber);
	}
	
	public static void main(String[] args) {
		cacheManager = new CacheManager();
		cacheManager.put(new Integer(3), 10);
		cacheManager.put(new Integer(5), 10);
		cacheManager.put(new String("narayan"), 10);
		System.out.println(cacheManager);
	}
}
