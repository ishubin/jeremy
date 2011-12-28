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
package net.mindengine.jeremy.messaging.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.LanguageHandler;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.DeserializationConfig;
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
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(serializedString,type);
        } catch (Exception e) {
            DeserializationException de = new DeserializationException(e);
            de.setContent(serializedString);
            throw de;
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
