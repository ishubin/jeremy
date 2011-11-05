/*******************************************************************************
 * 2011 Ivan Shubin http://mindengine.net
 * 
 * This file is part of Mind-Engine Jeremy.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Mind-Engine Jeremy.  If not, see <http://www.gnu.org/licenses/>.
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
