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

import java.util.LinkedList;
import java.util.List;

public class ThreadContainer<T> implements ThreadListener<T> {

    private List<ThreadSample<T>> threads = new LinkedList<ThreadSample<T>>();
    private Executor<T> executor;
    private Listener listener;
    
    private int threadsNotStarted = 0;
    private int threadsCompleted = 0;
    private int threadsAborted = 0;
    private int threadsRunning = 0;
    
    /**
     * Maximum awaiting time in seconds 
     */
    private long timeout = 60;
    
    private int freeThreads = 0;
    
    public void addObject(T object) {
        if(executor==null) {
            throw new IllegalArgumentException("Executor is not specified");
        }
        getThreads().add(new ThreadSample<T>(object, executor, this));
    }


    public void setExecutor(Executor<T> executor) {
        this.executor = executor;
    }


    public Executor<T> getExecutor() {
        return executor;
    }


    @Override
    public void onThreadFinished(ThreadSample<T> object) {
        freeThreads++;
    }

    @Override
    public void onThreadStart(ThreadSample<T> object) {
        
    }


    public void startAllThreads() {
        freeThreads = 0;
        
        if(listener!=null) {
            listener.onStart();
        }
        
        for(ThreadSample<T> thread : getThreads()) {
            thread.start();
        }
        
        int size = getThreads().size();
        
        int time = 0;
        while(time<timeout && freeThreads < size) {
            try {
                Thread.sleep(1000);
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            time++;
        }
        
        if(listener!=null) {
            listener.onStop();
        }
        
        for(ThreadSample<T> thread : getThreads()) {
            if(thread.getStatus()==ThreadSample.NOT_STARTED) {
                threadsNotStarted++;
            }
            else if(thread.getStatus()==ThreadSample.ABORTED) {
                threadsAborted++;
            }
            else if(thread.getStatus()==ThreadSample.RUNNING) {
                threadsRunning++;
            }
            else if(thread.getStatus()==ThreadSample.COMPLETED) {
                threadsCompleted++;
            }
        }
    }


    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }


    public long getTimeout() {
        return timeout;
    }


    public void setThreads(List<ThreadSample<T>> threads) {
        this.threads = threads;
    }


    public List<ThreadSample<T>> getThreads() {
        return threads;
    }


    public int getFreeThreads() {
        return freeThreads;
    }


    public void setFreeThreads(int freeThreads) {
        this.freeThreads = freeThreads;
    }


    public int getThreadsNotStarted() {
        return threadsNotStarted;
    }


    public int getThreadsCompleted() {
        return threadsCompleted;
    }


    public int getThreadsAborted() {
        return threadsAborted;
    }


    public int getThreadsRunning() {
        return threadsRunning;
    }


    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public Listener getListener() {
        return listener;
    }

    
    
}
