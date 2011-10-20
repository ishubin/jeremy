package net.mindengine.jeremy.messaging;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.jeremy.exceptions.DeserializationException;


/**
 * Used to read objects from request and serialize object to response
 * @author soulrevax
 *
 */
public interface RequestResponseHandler {

    /**
     * Returns array of arguments for specified method
     * @param method
     * @param request
     * @return
     * @throws DeserializationException 
     */
    public Object[] getObjects(Method method, HttpServletRequest request) throws DeserializationException;
    
    /**
     * Serializes object to string
     * @param object
     * @return
     */
    public String serializeResponse(Object object); 
}
