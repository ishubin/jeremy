package net.mindengine.jeremy.cache;

import net.mindengine.jeremy.objects.SerialObject;

/**
 * Used in order to cache objects in memory
 * @author Ivan Shubin
 *
 */
public interface Cache {
    /**
     * Puts object to cache and generate unique id for object in cache 
     * @param myObject
     * @return Id of object in cache
     */
    public String cacheObject(SerialObject myObject);
    
    /**
     * Fetches object from cache 
     * @param objectIdInCache Id of object in cache
     * @return Object from cache
     */
    public Object retrieveObjectFromCache(String objectIdInCache);
    
    /**
     * Removes object with specified id from cache
     * @param objectIdInCache Id of object in cache
     */
    public void clearCache(String objectIdInCache);
}
