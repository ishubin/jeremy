package net.mindengine.jeremy.messaging;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.jeremy.cache.Cache;
import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;


/**
 * Used to read objects from request and serialize object to response
 * @author Ivan Shubin
 *
 */
public interface RequestResponseHandler extends Cache {

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
     * @throws SerializationException 
     */
    public String serializeResponse(Object object) throws SerializationException;

    
}
