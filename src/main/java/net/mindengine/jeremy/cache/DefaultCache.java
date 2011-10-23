package net.mindengine.jeremy.cache;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.mindengine.jeremy.objects.SerialObject;

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
    public synchronized String cacheObject(SerialObject myObject) {
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
