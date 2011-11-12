/*******************************************************************************
 * Copyright 2011 Ivan Shubin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.mindengine.jeremy.test.performance;

public class ThreadSample<T> implements Runnable {

    private T object;
    private Executor<T> executor;
    private ThreadListener<T> listener;
    
    public static final int NOT_STARTED = 0;
    public static final int RUNNING = 1;
    public static final int COMPLETED = 2;
    public static final int ABORTED = 3;
    
    private int status = NOT_STARTED; 
    
    private Thread thread;
    
    public ThreadSample(T object, Executor<T> executor, ThreadListener<T> listener) {
        this.object = object;
        this.executor = executor;
        this.listener = listener;
        this.thread = new Thread(this);
    }
    
    
    
    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        this.status = RUNNING;
        listener.onThreadStart(this);
        try {
            executor.execute(object);
            status = COMPLETED;
        } catch (Exception e) {
            status = ABORTED;
        }
        listener.onThreadFinished(this);
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public int getStatus() {
        return status;
    }
    
    
}
