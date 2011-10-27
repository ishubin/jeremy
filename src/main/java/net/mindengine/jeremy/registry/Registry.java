package net.mindengine.jeremy.registry;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.cache.DefaultCache;
import net.mindengine.jeremy.messaging.RequestResponseHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonRequestResponseHandler;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class Registry {

    private Server server;
    private Map<String, RemoteObject> remoteObjects = new ConcurrentHashMap<String, RemoteObject>();
    private RegistryServlet servlet;
    private RequestResponseHandler requestResponseHandler;
    
    public void start() throws Exception {
        server = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(8085);

        server.addConnector(connector);

        Context context = new Context();
        ServletHolder holder = new ServletHolder();
        servlet = new RegistryServlet();
        servlet.setRegistry(this);
        if(requestResponseHandler==null) {
            DefaultJsonRequestResponseHandler requestResponseHandler = new DefaultJsonRequestResponseHandler();
            this.requestResponseHandler = requestResponseHandler;
            requestResponseHandler.setCache(new DefaultCache());
        }
        servlet.setRequestResponseHandler(requestResponseHandler);
        holder.setServlet(servlet);
        context.addServlet(holder, "/*");
        
        server.addHandler(context);

        server.start();
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
    
    public void addObject(String name, Remote remoteObject) {
        if(remoteObject==null) {
            throw new IllegalArgumentException("Cannot add null objects");
        }
        RemoteObject object = new RemoteObject();
        object.setName(name);
        object.setObject(remoteObject);
        
        /*
         * Fetching list of all remote methods implemented in remote object
         */
        List<Class<?>> remoteInterfaces = getAllRemoteInterfaces(remoteObject.getClass());
        for(Class<?> remoteInterface : remoteInterfaces) {
            for(Method method : remoteInterface.getMethods()) {
                object.getRemoteMethods().put(method.getName(), method);
            }
        }
        
        if(object.getRemoteMethods().size()==0) {
            throw new IllegalArgumentException("Remote object "+remoteObject.getClass()+" doesn't have any remote methods");
        }
        
        getRemoteObjects().put(name, object);
    }
    
    public void removeObject(String name) {
        getRemoteObjects().remove(name);
    }
    
    /**
     * Searches for all remote interfaces which are represented in specified class
     * @param clazz
     * @return List of remote interfaces implemented in the specified clazz
     */
    public static List<Class<?>> getAllRemoteInterfaces(Class<?> clazz) {
        List<Class<?>> list = new LinkedList<Class<?>>();
        
        Class<?>[] interfaces = clazz.getInterfaces();
        for(Class<?> interfaceClass : interfaces) {
            if(Remote.class.isAssignableFrom(interfaceClass)){
                list.add(interfaceClass);
            }
        }
        
        Class<?>parentClazz = clazz.getSuperclass();
        if(parentClazz!=null) {
            list.addAll(getAllRemoteInterfaces(parentClazz));
        }
        return list;
    }

    public void setRequestResponseHandler(RequestResponseHandler requestResponseHandler) {
        this.requestResponseHandler = requestResponseHandler;
    }

    public RequestResponseHandler getRequestResponseHandler() {
        return requestResponseHandler;
    }

    public void setRemoteObjects(Map<String, RemoteObject> remoteObjects) {
        this.remoteObjects = remoteObjects;
    }

    public Map<String, RemoteObject> getRemoteObjects() {
        return remoteObjects;
    }


}
