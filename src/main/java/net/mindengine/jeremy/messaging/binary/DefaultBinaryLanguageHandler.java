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
package net.mindengine.jeremy.messaging.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.LanguageHandler;

public class DefaultBinaryLanguageHandler implements LanguageHandler {
    
    
    @Override
    public Object deserializeObject(String serializedString, Class<?> type) throws DeserializationException {
        if(serializedString!=null) {
            byte[] bytes = serializedString.getBytes();
            return this.deserializeObject(bytes, type);
        }
        return null;
    }

    @Override
    public String serializeResponse(Object object) throws SerializationException {
        byte[] bytes = this.serializeResponseToBytes(object);
        if(bytes!=null) {
            return new String(bytes);
        }
        return null;
    }

    @Override
    public String getMimeType() {
        return Client.APPLICATION_BINARY;
    }

    @Override
    public byte[] serializeResponseToBytes(Object object) throws SerializationException {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
            ObjectOutputStream oos = new ObjectOutputStream(bos) ;
            oos.writeObject(object);
            oos.close();
            return bos.toByteArray();
        }
        catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public Object deserializeObject(byte[] content, Class<?> type) throws DeserializationException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(content));
            return ois.readObject();
        }
        catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

}
