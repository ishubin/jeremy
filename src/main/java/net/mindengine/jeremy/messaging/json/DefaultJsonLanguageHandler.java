package net.mindengine.jeremy.messaging.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.LanguageHandler;

import org.codehaus.jackson.map.ObjectMapper;

public class DefaultJsonLanguageHandler implements LanguageHandler {

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
    public Object deserializeObject(String serializedString, Class<?>type) throws DeserializationException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(serializedString,type);
        } catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public Object deserializeObject(byte[] content, Class<?> type) throws DeserializationException {
        String str = new String(content);
        return this.deserializeObject(str, type);
    }
    
    @Override
    public byte[] serializeResponseToBytes(Object object) throws SerializationException {
        String str = this.serializeResponse(object);
        if(str!=null) {
            return str.getBytes();
        }
        return null;
    }
}
