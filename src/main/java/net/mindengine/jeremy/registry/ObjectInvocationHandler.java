package net.mindengine.jeremy.registry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import net.mindengine.jeremy.bin.Binary;
import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.messaging.RequestResponseHandler;

public class ObjectInvocationHandler implements InvocationHandler {

    private Client client;
    private RequestResponseHandler requestResponseHandler;
    private String urlToRemoteObject;
    private String url;

    public static Object createProxyRemoteObject(String url, String objectName, Class<?> interfaceClass, Client client,
            RequestResponseHandler requestResponseHandler) {
        ObjectInvocationHandler objectInvocationHandler = new ObjectInvocationHandler();
        objectInvocationHandler.setClient(client);
        objectInvocationHandler.setUrl(url);
        objectInvocationHandler.setRequestResponseHandler(requestResponseHandler);
        objectInvocationHandler.setUrlToRemoteObject(url + "/" + objectName);
        Object object = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
                objectInvocationHandler);
        return object;
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
        if (args != null) {
            for (Object argument : args) {

                if (argument != null && Binary.class.isAssignableFrom(argument.getClass())) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
                    ObjectOutputStream out = new ObjectOutputStream(bos) ;
                    out.writeObject(object);
                    out.close();
                    
                    HttpResponse response = client.sendMultiPartBinaryRequest(url+"/~bin", "argument"+i, new ByteArrayInputStream(bos.toByteArray()));
                    if(response.getStatus()<400) {
                        Map<String, String> map = (Map<String, String>) requestResponseHandler.deserializeObject(response.getContent(), new HashMap<String, String>().getClass());
                        String key = map.get("argument"+i);
                        if(key==null) {
                            throw new ConnectionError("Cannot find key for the uploaded binary object");
                        }
                        params.put("arg"+i, "~"+key);
                    }
                    else throw new ConnectionError("Cannot upload binary object for remote method argument");
                    
                } else {
                    params.put("arg" + i, requestResponseHandler.serializeResponse(argument));
                }
                i++;
            }
        }

        HttpResponse response = client.postRequest(fullUrl, params);

        if (response.getStatus() < 400) {
            if (!method.getReturnType().equals(Void.TYPE)) {
                return requestResponseHandler.deserializeObject(response.getContent(), method.getReturnType());
            } else
                return null;
        } else {
            // TODO handle remote exceptions
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
