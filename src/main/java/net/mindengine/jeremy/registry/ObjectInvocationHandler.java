/*******************************************************************************
* Copyright 2012 Ivan Shubin http://mindengine.net
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
import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

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
    
    public Map<String, String> generateHttpHeaders(String languageHandler) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Client.LANGUAGE_HEADER, languageHandler);
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
        Map<String, String> headers = generateHttpHeaders(lookup.getDefaultLanguage());
        
        if (args != null) {
            for (Object argument : args) {

                if (argument != null && argument instanceof File) {
                    //Serializing argument to binary data
                    byte[] bytes = FileUtils.readFileToByteArray((File)argument);
                    
                    HttpResponse response = client.sendMultiPartBinaryRequest(url+"/~file", "argument"+i, new ByteArrayInputStream(bytes), generateHttpHeaders(lookup.getDefaultLanguage()));
                    if(response.getStatus()<400) {
                        Map<String, String> map = (Map<String, String>) languageHandler.deserializeObject(response.getBytes(), new HashMap<String, String>().getClass());
                        String key = map.get("argument"+i);
                        if(key==null) {
                            throw new ConnectionError("Cannot find key for the uploaded file");
                        }
                        params.put("arg"+i, "~"+key);
                        headers.put(Client.CLASS_PATH+"-"+i, argument.getClass().getName());
                    }
                    else throw new ConnectionError("Cannot upload binary object for remote method argument");
                } else {
                    params.put("arg" + i, languageHandler.serializeResponse(argument));
                    if(argument!=null) {
                        headers.put(Client.CLASS_PATH+"-"+i, argument.getClass().getName());
                    }
                }
                i++;
            }
        }

        HttpResponse response = client.postRequest(fullUrl, params, headers);
        
        if (response.getStatus() < 400) {
            if (!method.getReturnType().equals(Void.TYPE)) {
                
                String returnedClassName = response.getHeaders().get(Client.CLASS_PATH);
                Class<?> returnedClass = method.getReturnType();
                if(returnedClassName!=null) {
                    returnedClass = Class.forName(returnedClassName);
                }
                
                if(returnedClass.equals(File.class)) {
                    //Saving response to temp file
                    String fileName = UUID.randomUUID().toString().replace("-", "");
                    File tmpFile = File.createTempFile(fileName, ".tmp");
                    FileUtils.writeByteArrayToFile(tmpFile, response.getBytes());
                    return tmpFile;
                }
                else {
                    LanguageHandler returnLanguageHandler = lookup.getLanguageHandler(response.getLanguage());
                    return returnLanguageHandler.deserializeObject(response.getBytes(), returnedClass);
                }
            } else
                return null;
        } 
        else {
            //Trying to deserialize exception
            
            String errorClassName = response.getHeaders().get(Client.CLASS_PATH);
            if(errorClassName!=null) {
                LanguageHandler returnLanguageHandler = lookup.getLanguageHandler(response.getLanguage());
                
                Class<?>errorClass = Class.forName(errorClassName);
                
                Throwable throwable = null;
                throwable = (Throwable) returnLanguageHandler.deserializeObject(response.getBytes(), errorClass);
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
