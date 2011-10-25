package net.mindengine.jeremy.registry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.mindengine.jeremy.client.Client;

public class ObjectInvocationHandler implements InvocationHandler{

    private Client client;
    
    @Override
    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        //TODO implement remote method invocation
        return null;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

}
