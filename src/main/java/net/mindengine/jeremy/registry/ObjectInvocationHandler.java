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
package net.mindengine.jeremy.registry;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import net.mindengine.jeremy.bin.Binary;
import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.messaging.LanguageHandler;

public class ObjectInvocationHandler implements InvocationHandler {

    private Client client;
    private String urlToRemoteObject;
    private String url;
    private Lookup lookup;

    public static Object createProxyRemoteObject(String url, String objectName, Class<?> interfaceClass, Client client,
            Lookup lookup) {
        ObjectInvocationHandler objectInvocationHandler = new ObjectInvocationHandler();
        objectInvocationHandler.setClient(client);
        objectInvocationHandler.setUrl(url);
        objectInvocationHandler.setLookup(lookup);
        objectInvocationHandler.setUrlToRemoteObject(url + "/" + objectName);
        Object object = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
                objectInvocationHandler);
        return object;
    }
    
    public Map<String, String> generateHttpHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Client.LANGUAGE_HEADER, lookup.getDefaultLanguage());
        return headers;
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {

        if (urlToRemoteObject == null) {
            throw new IllegalArgumentException("The proxy object is not registered");
        }

        String fullUrl = urlToRemoteObject + "/" + method.getName();

        // Serializing remote method arguments
        int i = 0;
        Map<String, String> params = new HashMap<String, String>();
        LanguageHandler languageHandler = lookup.getLanguageHandler(lookup.getDefaultLanguage());
        if (args != null) {
            for (Object argument : args) {

                if (argument != null && Binary.class.isAssignableFrom(argument.getClass())) {
                    //Serializing argument to binary data
                    byte[]bytes = lookup.getLanguageHandler(Client.LANGUAGE_BINARY).serializeResponseToBytes(argument);
                    
                    HttpResponse response = client.sendMultiPartBinaryRequest(url+"/~bin", "argument"+i, new ByteArrayInputStream(bytes), generateHttpHeaders());
                    if(response.getStatus()<400) {
                        Map<String, String> map = (Map<String, String>) languageHandler.deserializeObject(response.getContent(), new HashMap<String, String>().getClass());
                        String key = map.get("argument"+i);
                        if(key==null) {
                            throw new ConnectionError("Cannot find key for the uploaded binary object");
                        }
                        params.put("arg"+i, "~"+key);
                    }
                    else throw new ConnectionError("Cannot upload binary object for remote method argument");
                    
                } else {
                    params.put("arg" + i, languageHandler.serializeResponse(argument));
                }
                i++;
            }
        }

        HttpResponse response = client.postRequest(fullUrl, params, generateHttpHeaders());
        
        if (response.getStatus() < 400) {
            if (!method.getReturnType().equals(Void.TYPE)) {
                
                LanguageHandler returnLanguageHandler = lookup.getLanguageHandler(response.getLanguage());
                if(response.getBytes()!=null) {
                    return returnLanguageHandler.deserializeObject(response.getBytes(), method.getReturnType());
                }
                else return returnLanguageHandler.deserializeObject(response.getContent(), method.getReturnType());
            } else
                return null;
        } 
        else {
            //Trying to deserialize exception
            
            String errorClassName = response.getHeaders().get(Client.ERROR_TYPE_HEADER);
            if(errorClassName!=null) {
                LanguageHandler returnLanguageHandler = lookup.getLanguageHandler(response.getLanguage());
                
                Class<?>errorClass = Class.forName(errorClassName);
                
                Throwable throwable = null;
                if(response.getBytes()!=null) {
                    throwable = (Throwable) returnLanguageHandler.deserializeObject(response.getBytes(), errorClass);
                }
                else throwable = (Throwable) returnLanguageHandler.deserializeObject(response.getContent(), errorClass);
                throw throwable;
            }
            else throw new RuntimeException("Cannot process remote error");
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }


    public void setUrlToRemoteObject(String urlToRemoteObject) {
        this.urlToRemoteObject = urlToRemoteObject;
    }

    public String getUrlToRemoteObject() {
        return urlToRemoteObject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Lookup getLookup() {
        return lookup;
    }

    public void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }

}
