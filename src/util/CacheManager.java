package util;

import java.util.HashMap;

public class CacheManager {
	private static HashMap<Object, CachedObject> cache = new HashMap<Object, CachedObject>();

	public void putInCache(CachedObject objectToCache) {
		cache.put(objectToCache.getIdentifier(), objectToCache);
	}

	public void removeFromCache(Object identifier) {
		cache.remove(identifier);
	}

	public CachedObject getFromCache(Object identifier) {
		CachedObject cachedObject = cache.get(identifier);

		if (cachedObject == null){
			System.out.println("Cache Miss! Object for id - " + identifier + " is not in the cahce" );
			return null;
			
		}
		if (cachedObject.isExpired())
		{
			this.removeFromCache(identifier);
			System.out.println("Object for id - " + identifier + " is expired! It has been removed from the cache" );
			return null;
		}
		
		// Object is present in the cache and not expired, so return it
		return cachedObject;
	}
}
