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
package net.mindengine.jeremy.registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.messaging.LanguageHandler;


public class Lookup {

    private String url;
    private Client client;
    private Map<String, LanguageHandler> languageHandlers = new HashMap<String, LanguageHandler>();
    private String defaultLanguage = Client.APPLICATION_BINARY;
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
                
                String[] remoteMethods = (String[]) languageHandler.deserializeObject(httpResponse.getContent(), String[].class);
                
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
            else throw new ConnectionError(httpResponse.getContent());
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
