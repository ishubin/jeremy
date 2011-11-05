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
