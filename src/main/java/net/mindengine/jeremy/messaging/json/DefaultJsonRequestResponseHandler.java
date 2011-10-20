package net.mindengine.jeremy.messaging.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.RequestResponseHandler;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
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
    
    
    @Override
    public Object[] getObjects(Method method, HttpServletRequest request) throws DeserializationException {
        try {
            Class<?>[]types = method.getParameterTypes();
            if(types!=null && types.length>0) {
                
                Object[] objects = new Object[types.length];
                
                for(int i=0;i<types.length; i++) {
                    String argumentString = request.getParameter("arg"+i);
                    //In case if argument http parameter wasn't specified it should be treated as null
                    if(argumentString == null) {
                        objects[i] = null;
                    }
                    else {
                        ObjectMapper mapper = new ObjectMapper();
                        objects[i] = mapper.readValue(argumentString, types[i]);
                    }
                }
                return objects;
            }
        }
        catch (Exception e) {
            throw new DeserializationException(e);
        }
        return null;
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

}
