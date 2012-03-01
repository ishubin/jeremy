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

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.Map;


import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.messaging.LanguageHandler;


public class Lookup {

    private String url;
    private Client client;
    private Map<String, LanguageHandler> languageHandlers = new HashMap<String, LanguageHandler>();
    private String defaultLanguage = Client.LANGUAGE_JSON;
    private Map<String, Map<Class<?>, Object>> cashedRemoteObjects = new HashMap<String, Map<Class<?>, Object>>();
    
    public Lookup() {
    }
    
    public Lookup(String url) {
        super();
        this.url = url;
    }

    
    public void addLanguageHandler(String contentType, LanguageHandler languageHandler) {
        this.languageHandlers.put(contentType, languageHandler);
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
    
    
    private Object getObjectFromCache(String objectName, Class<?> interfaceClass) {
        Map<Class<?>,Object> map = cashedRemoteObjects.get(objectName);
        if(map!=null) {
            return map.get(interfaceClass);
        }
        return null;
    }
    
    private void putObjectToCache(String objectName, Class<?>interfaceClass, Object object) {
        Map<Class<?>,Object> map = cashedRemoteObjects.get(objectName);
        if(map==null) {
            map = new HashMap<Class<?>, Object>();
            cashedRemoteObjects.put(objectName, map);
        }
        map.put(interfaceClass, object);
    }

    public LanguageHandler getLanguageHandler(String contentType) {
        if(contentType!=null && languageHandlers.containsKey(contentType)){
            return languageHandlers.get(contentType);
        }
        else return languageHandlers.get(defaultLanguage);
    }
    
    public Map<String, String> generateHttpHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", getDefaultLanguage());
        return headers;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getRemoteObject(String objectName, Class<T> interfaceClass) throws RemoteObjectIsNotFoundException, ConnectionError{
        
        //Checking if there is already such object in cache
        Object objectFromCache = getObjectFromCache(objectName, interfaceClass);
        if(objectFromCache!=null) {
            return (T) objectFromCache;
        }
        
        LanguageHandler languageHandler = getLanguageHandler(defaultLanguage);
        
        if(client==null) {
            client = new Client();
        }
        
        if(!Remote.class.isAssignableFrom(interfaceClass)) 
            throw new IllegalArgumentException("Cannot create a remote object with "+interfaceClass.getName()+". Should support "+Remote.class.getName()+" interface");
        
        try {
            HttpResponse httpResponse  = client.getRequest(url+"/"+objectName+"/~", null, generateHttpHeaders());
            if(httpResponse.getStatus()<=300) {
                
                String[] remoteMethods = (String[]) languageHandler.deserializeObject(httpResponse.getBytes(), String[].class);
                
                Method[] declaredMethods = interfaceClass.getMethods();
                for(Method method : declaredMethods) {
                    boolean found = false;
                    for(int i=0;i<remoteMethods.length && !found; i++) {
                        if(method.getName().equals(remoteMethods[i])) {
                            found = true;
                        }
                    }
                    if(!found) {
                        throw new RemoteObjectIsNotFoundException("Remote object '"+objectName+"' doesn't support specified interface "+interfaceClass.getName()+". Remote object doesn't support '"+method.getName()+"' method");
                    }
                }
                
                T object =  (T)ObjectInvocationHandler.createProxyRemoteObject(url, objectName, interfaceClass, client, this);
                /**
                 * Putting just created proxy object to cache, so next time it will not be generated again
                 */
                putObjectToCache(objectName, interfaceClass, object);
                return object;
            }
            else if(httpResponse.getStatus()==404) {
                throw new RemoteObjectIsNotFoundException("There is no remote object with name '"+objectName+"'");
            }
            else throw new ConnectionError(new String(httpResponse.getBytes()));
        } 
        catch (Exception e) {
            throw new ConnectionError("Cannot reach remote server", e);
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public Map<String, LanguageHandler> getLanguageHandlers() {
        return languageHandlers;
    }

    public void setLanguageHandlers(Map<String, LanguageHandler> languageHandlers) {
        this.languageHandlers = languageHandlers;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultContentType) {
        this.defaultLanguage = defaultContentType;
    }
}
