package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.Remote;

public interface MyRemoteInterface extends Remote {

    public String getName();
    
    public void setName(String name);
    
    public SerialObject getSerialObject();
    
    public void setSerialObject(SerialObject serialObject);
}
