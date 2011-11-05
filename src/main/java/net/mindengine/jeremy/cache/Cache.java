/*******************************************************************************
 * 2011 Ivan Shubin http://mindengine.net
 * 
 * This file is part of Mind-Engine Jeremy.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Mind-Engine Jeremy.  If not, see <http://www.gnu.org/licenses/>.
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
