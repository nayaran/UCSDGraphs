package util;

import java.util.HashMap;

public class CacheManager {
	private static HashMap<Object, Cacheable> cache = new HashMap<Object, Cacheable>();

	private boolean DETAILED_STR_REPRESENTATION_ON = true;

	public void putInCache(Object objectToCache, int timeToLiveSeconds) {
		Cacheable cacheableObject = makeObjectCacheable(objectToCache, timeToLiveSeconds);
		if (cacheableObject != null) {
			Object identifier = cacheableObject.getIdentifier();
			cache.put(identifier, cacheableObject);
			// System.out.println("Added {" + identifier + " = " + cacheableObject + "} in
			// the cache");
		}
	}

	private Cacheable makeObjectCacheable(Object objectToCache, int timeToLiveSeconds) {
		SimpleCacheable cacheableObject = null;
		if (objectToCache != null) {
			cacheableObject = new SimpleCacheable(objectToCache, objectToCache.hashCode(), timeToLiveSeconds);
		}
		return cacheableObject;
	}

	public void removeFromCache(Object identifier) {
		cache.remove(identifier);
	}

	public Object getFromCache(Object identifier) {
		Cacheable cachedObject = cache.get(identifier);

		if (cachedObject == null) {
			// System.out.println("Cache Miss! Object for id - " + identifier + " is not in
			// the cahce" );
			return null;

		}
		if (cachedObject.isExpired()) {
			this.removeFromCache(identifier);
			System.out.println("Object for id - " + identifier + " is expired! It has been removed from the cache");
			return null;
		}

		// Object is present in the cache and not expired, so return it
		// System.out.println("Cache Hit! {" + cachedObject + "} found in cache!" );
		return cachedObject.getEnclosingObject();
	}

	public int getSize() {
		return this.cache.size();
	}

	public String toString() {
		String stringRepresentation = "Size of cache - " + this.getSize();
		if (DETAILED_STR_REPRESENTATION_ON) {
			if (this.getSize() > 0) {
				int objectCount = 1;
				stringRepresentation += "\nList of cached objects - \n";
				for (Object key : CacheManager.cache.keySet()) {
					stringRepresentation += objectCount + ". " + this.getFromCache(key).toString() + "\n";
					objectCount++;
				}
			}
		}
		return stringRepresentation;
	}
}
