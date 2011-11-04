package net.mindengine.jeremy.test.performance;

public interface Executor<T> {

    public void execute(T object) throws Exception;
}
