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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCache implements Cache {

    private Map<String, Object> objects = new ConcurrentHashMap<String, Object>();
    
    
    private String getUniqueKey() {
        int tries = 1000;
        while(tries>0){
            tries--;
            
            String key = UUID.randomUUID().toString().replace("-", "");
            if(!objects.containsKey(key)) {
                return key;
            }
        }
        throw new RuntimeException("Cannot generate unique identifier");
    }
    
    @Override
    public synchronized String cacheObject(Object myObject) {
        String key = getUniqueKey();
        objects.put(key, myObject);
        return key;
    }

    @Override
    public void clearCache(String objectIdInCache) {
        objects.remove(objectIdInCache);
        
    }

    @Override
    public Object retrieveObjectFromCache(String objectIdInCache) {
        return objects.get(objectIdInCache);
    }

}
