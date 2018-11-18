import java.util.LinkedList;

/**
 * Cache class - a LinkList based implementation of a simple cache
 * @author matthewmerris
 *
 * @param <T> - generic type of objects stored in the cache
 */
public class Cache<T> {
	private int maxSize;								// cache size
	private int cacheHit;								// cache hit - number of times an object is found in the cache
	private LinkedList<T> list;							// underlying data structure for implementing a cache

	/**
	 * Cache construct, takes an integer value as a parameter to specify the size of cache
	 * being constructed.
	 * @param size - integer value for the size of cache
	 */
	public Cache(int size) {
		list = new LinkedList<T>();						// create an empty LinkedList object
		maxSize = size;									// set maxSize to the specified size
		cacheHit = 0;
	}

	/**
	 * Check the cache for the specified object
	 * @param object
	 * @return - boolean true if object is in the cache, false if it isnt
	 */
	public boolean getObject(T object) {
		if(list.contains(object))
			cacheHit++;									// increment cache hit

		return list.contains(object);
	}

	/**
	 * Adds specified object to the cache, i.e. to the front
	 * @param object - generic data type used, object to add to the cache
	 */
	public void addObject(T object) {
		if(list.size() == maxSize)
			list.removeLast();							// if size of the cache has reached maxSize, bump the last object in the cache
		list.addFirst(object);							// add object to the front of the cache
	}

	/**
	 * Removes specified object from cache
	 * @param object - generic data type used, object to remove from the cache
	 */
	public void removeObject(T object) {
		list.remove(object);
	}

	/**
	 * Getter method for retreaving the number of cache hits
	 * @return - integer number of cache hits
	 */
	public int getHits(){
		int hits = cacheHit;
		return hits;
	}

}
