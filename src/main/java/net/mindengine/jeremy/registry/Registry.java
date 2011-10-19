package net.mindengine.jeremy.registry;

import java.util.HashMap;
import java.util.Map;

import net.mindengine.jeremy.Remote;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class Registry {

    private Server server;
    private Map<String, Object> objects = new HashMap<String, Object>();
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
    
    public static void main(String[] args) throws Exception {
        Registry registry = new Registry();
        registry.start();
    }

    public void addObject(String name, Remote remoteObject) {
        if(remoteObject==null) {
            throw new IllegalArgumentException("Cannot add null objects");
        }
        objects.put(name, remoteObject);
    }
    
    public void removeObject(String name) {
        objects.remove(name);
    }

    public void setRequestResponseHandler(RequestResponseHandler requestResponseHandler) {
        this.requestResponseHandler = requestResponseHandler;
    }

    public RequestResponseHandler getRequestResponseHandler() {
        return requestResponseHandler;
    }

}
