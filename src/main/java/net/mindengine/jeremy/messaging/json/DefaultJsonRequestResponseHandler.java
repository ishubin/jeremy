package net.mindengine.jeremy.messaging.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.mindengine.jeremy.cache.Cache;
import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.RequestResponseHandler;

import org.codehaus.jackson.map.ObjectMapper;

public class DefaultJsonRequestResponseHandler implements RequestResponseHandler {

    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }
    
    private Cache cache;
    
    
    

    @Override
    public String serializeResponse(Object object) throws SerializationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public String cacheObject(Object object) {
        verifyCache();
        return cache.cacheObject(object);
    }


    @Override
    public void clearCache(String objectIdInCache) {
        verifyCache();
        cache.clearCache(objectIdInCache);
    }


    @Override
    public Object retrieveObjectFromCache(String objectIdInCache) {
        verifyCache();
        return cache.retrieveObjectFromCache(objectIdInCache);
    }


    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void verifyCache() {
        if(cache==null) {
            throw new NullPointerException("Cache wasn't specified");
        }
    }

    public Cache getCache() {
        return cache;
    }


    @Override
    public Object deserializeObject(String serializedString, Class<?>type) throws DeserializationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(serializedString,type);
        } catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

}
