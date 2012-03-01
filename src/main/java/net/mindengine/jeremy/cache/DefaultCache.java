/*******************************************************************************
* Copyright 2012 Ivan Shubin http://mindengine.net
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
