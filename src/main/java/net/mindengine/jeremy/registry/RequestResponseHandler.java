package net.mindengine.jeremy.registry;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;


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
     */
    public Object[] getObjects(Method method, HttpServletRequest request);
    
    /**
     * Serializes object to string
     * @param object
     * @return
     */
    public String serializeResponse(Object object); 
}
