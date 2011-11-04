package net.mindengine.jeremy.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.messaging.binary.DefaultBinaryLanguageHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonLanguageHandler;
import net.mindengine.jeremy.objects.MyObject;
import net.mindengine.jeremy.objects.PerformanceObject;
import net.mindengine.jeremy.objects.PerformanceObjectInterface;
import net.mindengine.jeremy.registry.Lookup;
import net.mindengine.jeremy.registry.Registry;
import net.mindengine.jeremy.starter.RegistryStarter;

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
        PerformanceObjectInterface object =  lookup.getRemoteObject("remoteObject", PerformanceObjectInterface.class);
        
        object.executeSimpleMethod();
    }
}
