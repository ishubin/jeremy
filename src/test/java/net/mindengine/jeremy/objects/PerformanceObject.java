package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.bin.RemoteFile;

public class PerformanceObject implements PerformanceObjectInterface{

    private int executeSimpleMethodCount = 0;
    
    @Override
    public void executeSimpleMethod() throws InterruptedException {
        executeSimpleMethodCount++;
        Thread.sleep(5000);
    }

    @Override
    public void executeMethodWithBinaries(RemoteFile file) throws InterruptedException {
        Thread.sleep(5000);
    }

    public void setExecuteSimpleMethodCount(int executeSimpleMethodCount) {
        this.executeSimpleMethodCount = executeSimpleMethodCount;
    }

    public int getExecuteSimpleMethodCount() {
        return executeSimpleMethodCount;
    }

}
