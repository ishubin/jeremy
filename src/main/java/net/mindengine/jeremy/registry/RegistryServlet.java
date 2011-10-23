package net.mindengine.jeremy.registry;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFound;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.RequestResponseHandler;


public class RegistryServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 8419855038481651498L;

    private RequestResponseHandler requestResponseHandler;
    private Registry registry;
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json");
        
        String uri = request.getRequestURI();
        if(uri.endsWith("/")) {
            uri = uri.substring(0, uri.length()-1);
        }
        
        
        
        Object output = null;
        if(uri.endsWith("~")) {
            if(uri.matches("/.*/.*/~")) {
                //TODO
            }
            else if(uri.matches("/.*/~")) {
                output = getListOfAllRemoteMethods(uri);
            }
            else if(uri.matches("/~")) {
                output = getListOfAllObjects();
            }
        }
        else if(uri.matches("/.*/.*")) {
          //TODO
        }
        else {
            PrintWriter out = response.getWriter();
            response.setStatus(404);
            out.print("\"Not found\"");
            out.flush();
            out.close();
            return;
        }
        
        
        PrintWriter out = response.getWriter();
        try {
            String body = requestResponseHandler.serializeResponse(output);
            if(output instanceof Throwable) {
                response.setStatus(400);
                //TODO wrap output into exception wrapper so the client could see the classpath of thrown exception
            }
            else {
                response.setStatus(200);
                out.print(body);
            }
        } catch (SerializationException e) {
            response.setStatus(422);
            out.print("\"Unprocessable Entity\"");
        }
        
        out.flush();
        out.close();
    }
    
    
    private Object getListOfAllRemoteMethods(String uri) {
        Pattern pattern = Pattern.compile("/(.*?)/~");
        Matcher m = pattern.matcher(uri);
        while (m.find()) {
            String name = m.group(1);
            RemoteObject object = registry.getRemoteObjects().get(name);
            if(object!=null) {
                 return object.getRemoteMethods().keySet();
            }
            return new RemoteObjectIsNotFound("There is no such remote object '"+name+"'");
        }
        return new IllegalArgumentException("Cannot find name of object in URL");
    }


    public String[] getListOfAllObjects() {
        return registry.getRemoteObjects().keySet().toArray(new String[]{});
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void setRequestResponseHandler(RequestResponseHandler requestResponseHandler) {
        this.requestResponseHandler = requestResponseHandler;
    }

    public RequestResponseHandler getRequestResponseHandler() {
        return requestResponseHandler;
    }


    public void setRegistry(Registry registry) {
        this.registry = registry;
    }


    public Registry getRegistry() {
        return registry;
    }
}
