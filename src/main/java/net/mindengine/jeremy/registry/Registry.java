package net.mindengine.jeremy.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.messaging.RequestResponseHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonRequestResponseHandler;
import net.mindengine.jeremy.objects.MyObject;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class Registry {

    private Server server;
    private Map<String, Object> remoteObjects = new ConcurrentHashMap<String, Object>();
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
            requestResponseHandler = new DefaultJsonRequestResponseHandler();
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
        getRemoteObjects().put(name, remoteObject);
    }
    
    public void removeObject(String name) {
        getRemoteObjects().remove(name);
    }

    public void setRequestResponseHandler(RequestResponseHandler requestResponseHandler) {
        this.requestResponseHandler = requestResponseHandler;
    }

    public RequestResponseHandler getRequestResponseHandler() {
        return requestResponseHandler;
    }

    public void setRemoteObjects(Map<String, Object> remoteObjects) {
        this.remoteObjects = remoteObjects;
    }

    public Map<String, Object> getRemoteObjects() {
        return remoteObjects;
    }

}
