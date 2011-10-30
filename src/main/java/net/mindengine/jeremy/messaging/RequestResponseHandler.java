package net.mindengine.jeremy.messaging;
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
     * Deserializes object from specified string
     * @param serializedString
     * @param type Class of the serialized object
     * @return
     * @throws DeserializationException
     */
    public Object deserializeObject(String serializedString, Class<?> type) throws DeserializationException;
    
    /**
     * Serializes object to string
     * @param object
     * @return
     * @throws SerializationException 
     */
    public String serializeResponse(Object object) throws SerializationException;

    
}
