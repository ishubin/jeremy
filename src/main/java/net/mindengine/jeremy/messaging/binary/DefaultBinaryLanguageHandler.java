/*******************************************************************************
 * Copyright 2011 Ivan Shubin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.mindengine.jeremy.messaging.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;

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
        return Client.LANGUAGE_BINARY;
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
