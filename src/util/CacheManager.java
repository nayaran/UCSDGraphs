package util;

import java.util.HashMap;

public class CacheManager {
	private static HashMap<Object, Cacheable> cache = new HashMap<Object, Cacheable>();
	private int size;

	public void putInCache(Object objectToCache, int timeToLiveSeconds) {
		Cacheable cacheableObject = makeObjectCacheable(objectToCache, timeToLiveSeconds);
		if(cacheableObject != null){
			cache.put(cacheableObject.getIdentifier(), cacheableObject);
			this.size += 1;
		}
	}

	private Cacheable makeObjectCacheable(Object objectToCache, int timeToLiveSeconds) {
		// TODO Auto-generated method stub
		SimpleCacheable cacheableObject = null;
		if (objectToCache != null) {
			cacheableObject = new SimpleCacheable(objectToCache, objectToCache.hashCode(), timeToLiveSeconds); 
		}
		return cacheableObject;
	}

	public void removeFromCache(Object identifier) {
		cache.remove(identifier);
		this.size -= 1;
	}

	public Object getFromCache(Object identifier) {
		Cacheable cachedObject = cache.get(identifier);

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
		return cachedObject.getEnclosingObject();
	}

	public int getSize() {
		return size;
	}

	public String toString() {
		String stringRepresentation = "Size of cache - " + this.getSize();
		if (this.getSize() > 0) {
			int objectCount = 1;
			stringRepresentation += "\nList of cached objects - \n" +
					"\nObject - " + objectCount + "\n";

			for (Object key : CacheManager.cache.keySet()) {
				stringRepresentation += this.getFromCache(key).toString() + "\n";
			}
		}
		return stringRepresentation;
	}
}
