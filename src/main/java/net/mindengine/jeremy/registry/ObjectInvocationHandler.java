package net.mindengine.jeremy.registry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.mindengine.jeremy.client.Client;

public class ObjectInvocationHandler implements InvocationHandler{

    private Client client;
    
    /**
     * Contains urls for each created proxy object
     */
    private Map<Object, String> remoteObjectUrlsMap = new ConcurrentHashMap<Object, String>();
    
    
    public Object createProxyRemoteObject(String urlToRemoteObject, Class<?> interfaceClass) {
        Object object = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{interfaceClass}, this);
        remoteObjectUrlsMap.put(object, urlToRemoteObject);
        return object;
    }
    
    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        //TODO LAST. implement remote method invocation
        
        String url = remoteObjectUrlsMap.get(object);
        if(url==null) {
            throw new IllegalArgumentException("The proxy object is not registered");
        }
        
        String fullUrl = url+"/"+method.getName();
        
        //TODO serialize all parameters. upload all files
        return null;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

}
