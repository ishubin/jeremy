package net.mindengine.jeremy.registry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.messaging.RequestResponseHandler;

public class ObjectInvocationHandler implements InvocationHandler{

    private Client client;
    private RequestResponseHandler requestResponseHandler;
    private String urlToRemoteObject;
    
    public static Object createProxyRemoteObject(String url, String objectName, Class<?> interfaceClass, Client client, RequestResponseHandler requestResponseHandler) {
        ObjectInvocationHandler objectInvocationHandler = new ObjectInvocationHandler();
        objectInvocationHandler.setClient(client);
        objectInvocationHandler.setRequestResponseHandler(requestResponseHandler);
        objectInvocationHandler.setUrlToRemoteObject(url+"/"+objectName);
        Object object = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, objectInvocationHandler);
        return object;
    }
    
    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        
        if(urlToRemoteObject==null) {
            throw new IllegalArgumentException("The proxy object is not registered");
        }
        
        String fullUrl = urlToRemoteObject+"/"+method.getName();
        
        //Serializing remote method arguments
        int i=0;
        Map<String, String> params = new HashMap<String, String>();
        if(args!=null) {
            for(Object argument : args) {
              //TODO Upload files and binary data to server
                params.put("arg"+i, requestResponseHandler.serializeResponse(argument));
                i++;
            }
        }
        
        HttpResponse response = client.postRequest(fullUrl, params);
        
        if(response.getStatus()<400) {
            if(!method.getReturnType().equals(Void.TYPE)) {
                return requestResponseHandler.deserializeObject(response.getContent(), method.getReturnType());
            }
            else return null;
        }
        else {
            //TODO handle remote exceptions
            throw new RuntimeException("Something is wrong:" + response.getContent());
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

    public void setUrlToRemoteObject(String urlToRemoteObject) {
        this.urlToRemoteObject = urlToRemoteObject;
    }

    public String getUrlToRemoteObject() {
        return urlToRemoteObject;
    }

}
