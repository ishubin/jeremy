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
        
        System.out.println("start");
        tc.startAllThreads();
        System.out.println("\nend");
        System.out.println("Time = "+(timeMarks[1] - timeMarks[0]));
        System.out.println("Total threads = "+(timeMarks[1] - timeMarks[0]));
        System.out.println("  > Completed = "+tc.getThreadsCompleted());
        System.out.println("  > Aborted = "+tc.getThreadsAborted());
        System.out.println("  > Not started = "+tc.getThreadsNotStarted());
        System.out.println("  > Hanging = "+tc.getThreadsRunning());
        
        long time = timeMarks[1] - timeMarks[0];
        
        assertEquals(threadsAmount, tc.getThreadsCompleted());
        assertTrue(time<11000);
        assertEquals(threadsAmount, remoteObject.getExecuteSimpleMethodCount());
        //TODO verify that on server there were all calls
    }
}
