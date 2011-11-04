package net.mindengine.jeremy.test.performance;

public interface ThreadListener<T> {

    public void onThreadStart(ThreadSample<T> object);
    
    public void onThreadFinished(ThreadSample<T> object);
}
