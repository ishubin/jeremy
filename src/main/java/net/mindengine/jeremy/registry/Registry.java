package net.mindengine.jeremy.registry;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;

public class Registry {

    private Server server;
    
    public void start() throws Exception {
        server = new Server();

        Connector connector = new SelectChannelConnector();
        connector.setPort(8085);

        server.addConnector(connector);

        Context context = new Context();
        context.addServlet(RegistryServlet.class, "/*");
        
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
}
