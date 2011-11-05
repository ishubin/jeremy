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
package net.mindengine.jeremy.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;

import net.mindengine.jeremy.bin.RemoteFile;
import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.messaging.binary.DefaultBinaryLanguageHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonLanguageHandler;
import net.mindengine.jeremy.objects.PerformanceObject;
import net.mindengine.jeremy.objects.PerformanceObjectInterface;
import net.mindengine.jeremy.registry.Lookup;
import net.mindengine.jeremy.registry.Registry;
import net.mindengine.jeremy.starter.RegistryStarter;
import net.mindengine.jeremy.test.performance.Executor;
import net.mindengine.jeremy.test.performance.Listener;
import net.mindengine.jeremy.test.performance.ThreadContainer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegistryPerformanceTest {

    
    private static RegistryStarter registryStarter;
    private static PerformanceObject remoteObject;
    
    private static final String url = "http://localhost:8086";
    private static Properties properties;
    
    private static Lookup lookup = new Lookup(url);
    
    private static int threadsAmount = 10;
    
    @BeforeClass
    public static void initializeRegistry() throws InterruptedException, FileNotFoundException, IOException, URISyntaxException {
        properties = new Properties();
        properties.load(new FileReader(new File(RegistryPerformanceTest.class.getResource("/performance-test.properties").toURI())));
        
        threadsAmount = Integer.parseInt(properties.getProperty("threads.amount"));
        
        
        registryStarter = new RegistryStarter();
        Registry registry = new Registry();
        registry.addLanguageHandler(Client.APPLICATION_JSON, new DefaultJsonLanguageHandler());
        registry.setDefaultLanguage(Client.APPLICATION_JSON);
        registry.setPort(8086);
        
        remoteObject = new PerformanceObject();
        registry.addObject("remoteObject", remoteObject);
        
        registryStarter.setRegistry(registry);
        registryStarter.startRegistry();
        Thread.sleep(2000);
        
        lookup = new Lookup(url);
        lookup.setDefaultLanguage(Client.APPLICATION_JSON);
        lookup.addLanguageHandler(Client.APPLICATION_BINARY, new DefaultBinaryLanguageHandler());
        lookup.addLanguageHandler(Client.APPLICATION_JSON, new DefaultJsonLanguageHandler());
    }
    
    @AfterClass
    public static void stopRegistry() {
        registryStarter.stopRegistry();
    }
    
    @Test
    public void testRemoteObjectLookup()  throws RemoteObjectIsNotFoundException, ConnectionError, InterruptedException{
        ThreadContainer<Integer> tc = new ThreadContainer<Integer>();
        
        tc.setExecutor(new Executor<Integer>() {
            @Override
            public void execute(Integer object) throws RemoteObjectIsNotFoundException, ConnectionError {
                PerformanceObjectInterface poi = lookup.getRemoteObject("remoteObject", PerformanceObjectInterface.class);
                if(poi==null) {
                    throw new NullPointerException("Remote Object is null");
                }
            }
        });
        
        for(int i=0;i<threadsAmount; i++) {
            tc.addObject((Integer)i);
        }
        
        final long[] timeMarks = new long[2];
        
        tc.setListener(new Listener() {
            
            @Override
            public void onStop() {
                timeMarks[1] = new Date().getTime();
            }
            
            @Override
            public void onStart() {
                timeMarks[0] = new Date().getTime();
            }
        });
        
        tc.startAllThreads();
        printInfo(tc, timeMarks);
        
        long time = timeMarks[1] - timeMarks[0];
        
        assertEquals(threadsAmount, tc.getThreadsCompleted());
        assertTrue(time<6000);
    }
    
    @Test
    public void testRemoteMethodExecution() throws RemoteObjectIsNotFoundException, ConnectionError, InterruptedException {
        
        ThreadContainer<PerformanceObjectInterface> tc = new ThreadContainer<PerformanceObjectInterface>();
        tc.setExecutor(new Executor<PerformanceObjectInterface>() {
            @Override
            public void execute(PerformanceObjectInterface object) throws InterruptedException {
                object.executeSimpleMethod();
            }
        });
        
        for(int i=0;i<threadsAmount; i++) {
            tc.addObject(lookup.getRemoteObject("remoteObject", PerformanceObjectInterface.class));
        }
        
        final long[] timeMarks = new long[2];
        
        tc.setListener(new Listener() {
            
            @Override
            public void onStop() {
                timeMarks[1] = new Date().getTime();
            }
            
            @Override
            public void onStart() {
                timeMarks[0] = new Date().getTime();
            }
        });
        
        tc.startAllThreads();
        printInfo(tc, timeMarks);
        
        long time = timeMarks[1] - timeMarks[0];
        
        assertEquals(threadsAmount, tc.getThreadsCompleted());
        assertTrue(time<11000);
        //Verifying that the remote method was actually invoked on server
        assertEquals(threadsAmount, remoteObject.getExecuteSimpleMethodCount());
    }
    
    @Test
    public void testRemoteMethodWithFileSending() throws RemoteObjectIsNotFoundException, ConnectionError, InterruptedException {
        
        ThreadContainer<PerformanceObjectInterface> tc = new ThreadContainer<PerformanceObjectInterface>();
        tc.setExecutor(new Executor<PerformanceObjectInterface>() {
            @Override
            public void execute(PerformanceObjectInterface object) throws InterruptedException, IOException, URISyntaxException {
                object.executeMethodWithBinaries(new RemoteFile(new File(getClass().getResource("/test-file.png").toURI())));
            }
        });
        
        for(int i=0;i<threadsAmount; i++) {
            tc.addObject(lookup.getRemoteObject("remoteObject", PerformanceObjectInterface.class));
        }
        
        final long[] timeMarks = new long[2];
        
        tc.setListener(new Listener() {
            
            @Override
            public void onStop() {
                timeMarks[1] = new Date().getTime();
            }
            
            @Override
            public void onStart() {
                timeMarks[0] = new Date().getTime();
            }
        });
        
        tc.startAllThreads();
        printInfo(tc, timeMarks);
        
        long time = timeMarks[1] - timeMarks[0];
        
        assertEquals(threadsAmount, tc.getThreadsCompleted());
        assertTrue(time<11000);
        //Verifying that the remote method was actually invoked on server
        assertEquals(threadsAmount, remoteObject.getExecuteMethodWithBinaries());
    }
    
    private void printInfo(ThreadContainer<?> tc, long[]timeMarks) {
        System.out.println("Time = "+(timeMarks[1] - timeMarks[0]));
        System.out.println("Total threads = "+(timeMarks[1] - timeMarks[0]));
        System.out.println("  > Completed = "+tc.getThreadsCompleted());
        System.out.println("  > Aborted = "+tc.getThreadsAborted());
        System.out.println("  > Not started = "+tc.getThreadsNotStarted());
        System.out.println("  > Hanging = "+tc.getThreadsRunning());
        
    }
}
