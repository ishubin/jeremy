package net.mindengine.jeremy.registry;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.jeremy.exceptions.RemoteMethodIsNotFoundException;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
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
        try {
            if(uri.endsWith("~")) {
                if(uri.matches("/.*/.*/~")) {
                    output = getListOfRemoteMethodArguments(uri);
                }
                else if(uri.matches("/.*/~")) {
                    output = getListOfAllRemoteMethods(uri);
                }
                else if(uri.matches("/~")) {
                    output = getListOfAllObjects();
                }
            }
            else if(uri.matches("/.*/.*")) {
              //TODO invoke remote method
            }
            else {
                PrintWriter out = response.getWriter();
                response.setStatus(404);
                out.print("\"Not found\"");
                out.flush();
                out.close();
                return;
            }
        }
        catch (Exception e) {
            output = e;
        }
        
        
        PrintWriter out = response.getWriter();
        try {
            String body = requestResponseHandler.serializeResponse(output);
            if(output instanceof Throwable) {
                response.setStatus(400);
                Throwable error = (Throwable) output;
                
                RemoteExceptionWrapper remoteException = new RemoteExceptionWrapper();
                remoteException.setType(error.getClass().getName());
                remoteException.setError(error);
                out.print(registry.getRequestResponseHandler().serializeResponse(remoteException));
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
    
    
    private RemoteMethodMetadata getListOfRemoteMethodArguments(String uri) throws RemoteObjectIsNotFoundException, RemoteMethodIsNotFoundException {
        Pattern pattern = Pattern.compile("/(.*?)/(.*?)/~");
        Matcher m = pattern.matcher(uri);
        while (m.find()) {
            String objectName = m.group(1);
            String methodName = m.group(2);
            
            RemoteObject object = registry.getRemoteObjects().get(objectName);
            if(object!=null) {
                 Method method = object.getRemoteMethods().get(methodName);
                 if(method!=null) {
                     RemoteMethodMetadata metadata = new RemoteMethodMetadata();
                     metadata.setMethod(method.getName());
                     metadata.setObject(objectName);
                     
                     List<TypeDescriptor> arguments = new LinkedList<TypeDescriptor>();
                     Class<?>[]argumentTypes =  method.getParameterTypes();
                     if(argumentTypes!=null) {
                         for(Class<?> argumentType : argumentTypes) {
                             arguments.add(TypeDescriptor.createDescriptor(argumentType));
                         }
                     }
                     
                     metadata.setArguments(arguments);
                     
                     if(method.getReturnType()!=null && !method.getReturnType().getName().equals("void")) {
                         metadata.setReturns(TypeDescriptor.createDescriptor(method.getReturnType()));
                     }
                     return metadata;
                 }
                 else throw new RemoteMethodIsNotFoundException("There is no such method '"+methodName+"' for object '"+objectName+"'"); 
            }
            else throw new RemoteObjectIsNotFoundException("There is no such remote object '"+objectName+"'");
        }
        throw new IllegalArgumentException("Cannot find name of object in URL");
    }


    private Collection<String> getListOfAllRemoteMethods(String uri) throws RemoteObjectIsNotFoundException {
        Pattern pattern = Pattern.compile("/(.*?)/~");
        Matcher m = pattern.matcher(uri);
        while (m.find()) {
            String name = m.group(1);
            RemoteObject object = registry.getRemoteObjects().get(name);
            if(object!=null) {
                 return object.getRemoteMethods().keySet();
            }
            throw new RemoteObjectIsNotFoundException("There is no such remote object '"+name+"'");
        }
        throw new IllegalArgumentException("Cannot find name of object in URL");
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
