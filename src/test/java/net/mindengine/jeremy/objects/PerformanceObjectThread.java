package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.bin.RemoteFile;

public class PerformanceObjectThread implements PerformanceObjectInterface{

    private PerformanceObjectInterface remoteObject;
    
    @Override
    public void executeSimpleMethod() throws InterruptedException {
        
    }

    @Override
    public void executeMethodWithBinaries(RemoteFile file) throws InterruptedException {
        // TODO Auto-generated method stub
        
    }

    public PerformanceObjectInterface getRemoteObject() {
        return remoteObject;
    }

    public void setRemoteObject(PerformanceObjectInterface remoteObject) {
        this.remoteObject = remoteObject;
    }

}
