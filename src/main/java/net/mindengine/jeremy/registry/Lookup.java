package net.mindengine.jeremy.registry;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.messaging.RequestResponseHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonRequestResponseHandler;


public class Lookup {

    private String url;
    private ObjectInvocationHandler invocationHandler;
    private Client client;
    private RequestResponseHandler requestResponseHandler; 
    
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
    

    @SuppressWarnings("unchecked")
    public <T> T getRemoteObject(String string, Class<T> interfaceClass) throws RemoteObjectIsNotFoundException, ConnectionError{
        if(client==null) {
            client = new Client();
        }
        if(invocationHandler==null) {
            invocationHandler = new ObjectInvocationHandler();
        }
        invocationHandler.setClient(client);
        
        if(requestResponseHandler == null) {
            requestResponseHandler = new DefaultJsonRequestResponseHandler();
        }
        
        if(!Remote.class.isAssignableFrom(interfaceClass)) 
            throw new IllegalArgumentException("Cannot create a remote object with "+interfaceClass.getName()+". Should support "+Remote.class.getName()+" interface");
        
        try {
            HttpResponse httpResponse  = client.getRequest(url+"/"+string+"/~", null);
            if(httpResponse.getStatus()<=300) {
                
                String[] remoteMethods = (String[]) requestResponseHandler.deserializeObject(httpResponse.getContent(), String[].class);
                
                Method[] declaredMethods =  interfaceClass.getMethods();
                for(Method method : declaredMethods) {
                    boolean found = false;
                    for(int i=0;i<remoteMethods.length && !found; i++) {
                        if(method.getName().equals(remoteMethods[i])) {
                            found = true;
                        }
                    }
                    if(!found) {
                        throw new RemoteObjectIsNotFoundException("Remote object '"+string+"' doesn't support specified interface "+interfaceClass.getName()+". Remote object doesn't support '"+method.getName()+"' method");
                    }
                }
                
                return (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
                
            }
            else if(httpResponse.getStatus()==404) {
                throw new RemoteObjectIsNotFoundException("There is no remote object with name '"+string+"'");
            }
            else throw new ConnectionError(httpResponse.getContent());
        } 
        catch (Exception e) {
            throw new ConnectionError("Cannot reach remote server", e);
        }
    }

    public void setInvocationHandler(ObjectInvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public ObjectInvocationHandler getInvocationHandler() {
        return invocationHandler;
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
