package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.bin.RemoteFile;

public interface MyAnotherRemoteInterface extends Remote{

    public Long getId();
    
    public void setLong(Long id); 
    
    public void uploadFile(RemoteFile file);
    
    public RemoteFile downloadFile();
}
