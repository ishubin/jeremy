package net.mindengine.jeremy.registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.messaging.RequestResponseHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonRequestResponseHandler;


public class Lookup {

    private String url;
    private Client client;
    private RequestResponseHandler requestResponseHandler; 
    private Map<String, Map<Class<?>, Object>> cashedRemoteObjects = new HashMap<String, Map<Class<?>, Object>>();
    
    public Lookup() {
    }
    
    public Lookup(String url) {
        super();
        this.url = url;
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

    @SuppressWarnings("unchecked")
    public <T> T getRemoteObject(String objectName, Class<T> interfaceClass) throws RemoteObjectIsNotFoundException, ConnectionError{
        
        //Checking if there is already such object in cache
        Object objectFromCache = getObjectFromCache(objectName, interfaceClass);
        if(objectFromCache!=null) {
            return (T) objectFromCache;
        }
        
        if(client==null) {
            client = new Client();
        }
        
        if(requestResponseHandler == null) {
            requestResponseHandler = new DefaultJsonRequestResponseHandler();
        }
        
        if(!Remote.class.isAssignableFrom(interfaceClass)) 
            throw new IllegalArgumentException("Cannot create a remote object with "+interfaceClass.getName()+". Should support "+Remote.class.getName()+" interface");
        
        try {
            HttpResponse httpResponse  = client.getRequest(url+"/"+objectName+"/~", null);
            if(httpResponse.getStatus()<=300) {
                
                String[] remoteMethods = (String[]) requestResponseHandler.deserializeObject(httpResponse.getContent(), String[].class);
                
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
                
                T object =  (T)ObjectInvocationHandler.createProxyRemoteObject(url, objectName, interfaceClass, client, requestResponseHandler);
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

    public void setRequestResponseHandler(RequestResponseHandler requestResponseHandler) {
        this.requestResponseHandler = requestResponseHandler;
    }

    public RequestResponseHandler getRequestResponseHandler() {
        return requestResponseHandler;
    }
}
