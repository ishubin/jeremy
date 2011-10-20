package net.mindengine.jeremy.messaging.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.jeremy.exceptions.DeserializationException;
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

    @Override
    public Object[] getObjects(Method method, HttpServletRequest request) throws DeserializationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Class<?>[]types = method.getParameterTypes();
            if(types!=null && types.length>0) {
                String requestBody = convertStreamToString(request.getInputStream());
                
                
                Object[]objects = mapper.readValue(requestBody, Object[].class);
                
                //Converting all deserialized object to the type of corresponding method argument
                if(objects==null || objects.length!=types.length) {
                    throw new DeserializationException("Argument size is not the same as in "+method.toGenericString());
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
    public String serializeResponse(Object object) {
        // TODO Auto-generated method stub
        return null;
    }

}
