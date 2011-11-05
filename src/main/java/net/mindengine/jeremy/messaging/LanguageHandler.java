/*******************************************************************************
 * 2011 Ivan Shubin http://mindengine.net
 * 
 * This file is part of Mind-Engine Jeremy.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Mind-Engine Jeremy.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.mindengine.jeremy.messaging;
import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;


/**
 * Used to read objects from request and serialize object to response
 * @author Ivan Shubin
 *
 */
public interface LanguageHandler {
    
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
    
    /**
     * Serializes object to bytes
     * @param object
     * @return
     * @throws SerializationException 
     */
    public byte[] serializeResponseToBytes(Object object) throws SerializationException;

    /**
     * Returns the MIME type which further will be used in "Content-Type" header
     * @return
     */
    public String getMimeType();

    /**
     * Deserializes object from specified bytes array
     * @param content
     * @param type
     * @return
     */
    public Object deserializeObject(byte[] content, Class<?> type) throws DeserializationException;
    
}
