package net.mindengine.jeremy.registry;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.RemoteMethodIsNotFoundException;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.RequestResponseHandler;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


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
        
        request.getMethod();
        
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
            else if (uri.equals("/~file")) {
                output = uploadBinaryFile(request);
            }
            else if(uri.matches("/.*/.*")) {
              output = invokeRemoteMethod(uri, request);
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
    
    
    private Object invokeRemoteMethod(String uri, HttpServletRequest request) throws RemoteObjectIsNotFoundException, DeserializationException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Pattern pattern = Pattern.compile("/(.*?)/(.*?)/~");
        Matcher m = pattern.matcher(uri);
        while (m.find()) {
            String objectName = m.group(1);
            String methodName = m.group(2);
            //Searching for remote object in local registry
            RemoteObject remoteObject =  registry.getRemoteObjects().get(objectName);
            if(remoteObject==null) {
                throw new RemoteObjectIsNotFoundException("Object with name '"+objectName+"' was not found");
            }
            
            Method method = remoteObject.getRemoteMethods().get(methodName);
            if(method==null) {
                throw new RemoteObjectIsNotFoundException("Object with name '"+objectName+"' doesn't have method '"+methodName+"'");
            }
            
          //Collecting arguments for the remote method
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] arguments = new Object[parameterTypes.length];
            for(int i=0; i<arguments.length; i++) {
                String parameter = request.getParameter("arg"+i);
                if(parameter!=null) {
                    //Checking whether it is a plain serialized object in request or it is a reference to an object in cache
                    if(parameter.startsWith("~")) {
                        //Fetching object from cache
                        String key = parameter.substring(1);
                        
                        arguments[i] = registry.getRequestResponseHandler().retrieveObjectFromCache(key);
                        if(arguments[i]==null){
                            throw new NullPointerException("Couldn't find argument in cache with a key "+key);
                        }
                    }
                    else {
                        arguments[i] = requestResponseHandler.deserializeObject(parameter, parameterTypes[i]);
                    }
                }
                else arguments[i] = null;
            }
            return method.invoke(remoteObject.getObject(), arguments);
        }
        throw new IllegalArgumentException("Cannot find name of object and remote method in URL");
    }


    @SuppressWarnings("unchecked")
    private Map<String, String> uploadBinaryFile(HttpServletRequest request) throws FileUploadException {
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        List<FileItem> files = upload.parseRequest(request);
        
        Map<String, String> cachedObjects = new HashMap<String, String>();
        if(files!=null && files.size()>0) {
            for(FileItem fileItem : files) {
                byte[] content = fileItem.get();
                String id = requestResponseHandler.cacheObject(content);
                cachedObjects.put(fileItem.getFieldName(), id);
            }
            return cachedObjects;
        }
        else throw new IllegalArgumentException("Can't find any file for upload");
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


    private String[] getListOfAllObjects() {
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
