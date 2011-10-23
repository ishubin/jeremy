package net.mindengine.jeremy.registry;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteObject {

    private String name;
    private Object object;
    private Map<String, Method> remoteMethods = new ConcurrentHashMap<String, Method>();
    public void setObject(Object object) {
        this.object = object;
    }
    public Object getObject() {
        return object;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setRemoteMethods(Map<String, Method> remoteMethods) {
        this.remoteMethods = remoteMethods;
    }
    public Map<String, Method> getRemoteMethods() {
        return remoteMethods;
    }
}
