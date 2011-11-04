package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.bin.RemoteFile;

public interface PerformanceObjectInterface extends Remote {

    public void executeSimpleMethod() throws InterruptedException;
    
    public void executeMethodWithBinaries(RemoteFile file) throws InterruptedException;
}
