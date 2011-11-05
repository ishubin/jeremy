/*******************************************************************************
 * 2011 Ivan Shubin http://mindengine.net
 * 
 * This file is part of Mind-Engine Jeremy.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Mind-Engine Jeremy.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.mindengine.jeremy.registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.cache.Cache;
import net.mindengine.jeremy.cache.DefaultCache;
import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.messaging.LanguageHandler;
import net.mindengine.jeremy.messaging.binary.DefaultBinaryLanguageHandler;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class Registry {

    private Server server;
    private Map<String, RemoteObject> remoteObjects = new ConcurrentHashMap<String, RemoteObject>();
    private RegistryServlet servlet;
    private Cache objectCache;
    private int port = 80;
    
    /**
     * Map of language-handlers with Content-Type as a key.
     */
    private Map<String, LanguageHandler> languageHandlers = new HashMap<String, LanguageHandler>();
    
    
    private String defaultLanguage = Client.APPLICATION_BINARY;
    
    public void addLanguageHandler(String language, LanguageHandler languageHandler) {
        this.languageHandlers.put(language, languageHandler);
    }
    
    
    public void start() throws Exception {
        
        if(objectCache==null) {
            objectCache = new DefaultCache();
        }
        
        server = new Server();
        
        Connector connector = new SelectChannelConnector();
        connector.setPort(port);

        server.addConnector(connector);

        Context context = new Context();
        ServletHolder holder = new ServletHolder();
        servlet = new RegistryServlet();
        servlet.setRegistry(this);
        
        if(!getLanguageHandlers().containsKey(Client.APPLICATION_BINARY)) {
            getLanguageHandlers().put(Client.APPLICATION_BINARY, new DefaultBinaryLanguageHandler());
        }
        
        holder.setServlet(servlet);
        context.addServlet(holder, "/*");
        
        server.addHandler(context);

        server.start();
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
    
    /**
     * Returns either language-handler for specified contentType either default language-handler 
     * @param contentType
     * @return
     */
    public LanguageHandler getLanguageHandler(String contentType) {
        if(contentType!=null && languageHandlers.containsKey(contentType)) {
            return languageHandlers.get(contentType);
        }
        else return languageHandlers.get(defaultLanguage);
    }
    
    public void addObject(String name, Remote remoteObject) {
        if(remoteObject==null) {
            throw new IllegalArgumentException("Cannot add null objects");
        }
        if(name==null || name.isEmpty()) {
            throw new IllegalArgumentException("Object name should not be empty");
        }
        if(name.startsWith("~")) {
            throw new IllegalArgumentException("Object name is invalid");
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

    public void setRemoteObjects(Map<String, RemoteObject> remoteObjects) {
        this.remoteObjects = remoteObjects;
    }

    public Map<String, RemoteObject> getRemoteObjects() {
        return remoteObjects;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultContentType) {
        this.defaultLanguage = defaultContentType;
    }

    public Map<String, LanguageHandler> getLanguageHandlers() {
        return languageHandlers;
    }

    public void setLanguageHandlers(Map<String, LanguageHandler> languageHandlers) {
        this.languageHandlers = languageHandlers;
    }

    public Cache getObjectCache() {
        return objectCache;
    }

    public void setObjectCache(Cache objectCache) {
        this.objectCache = objectCache;
    }


    public int getPort() {
        return port;
    }


    public void setPort(int port) {
        this.port = port;
    }


}
