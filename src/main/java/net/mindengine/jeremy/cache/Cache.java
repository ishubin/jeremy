/*******************************************************************************
 * Copyright 2011 Ivan Shubin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.mindengine.jeremy.cache;


/**
 * Used in order to cache objects in memory
 * @author Ivan Shubin
 *
 */
public interface Cache {
    /**
     * Puts object to cache and generate unique id for object in cache 
     * @param object
     * @return Id of object in cache
     */
    public String cacheObject(Object object);
    
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
