package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.bin.RemoteFile;

public class PerformanceObject implements PerformanceObjectInterface{

    @Override
    public void executeSimpleMethod() throws InterruptedException {
        Thread.sleep(5000);
    }

    @Override
    public void executeMethodWithBinaries(RemoteFile file) throws InterruptedException {
        Thread.sleep(5000);
    }

}
