package net.mindengine.jeremy.tests;

import net.mindengine.jeremy.objects.MyObject;
import net.mindengine.jeremy.registry.Registry;
import net.mindengine.jeremy.starter.RegistryStarter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegistryIntegrationTest {

    
    private static RegistryStarter registryStarter;
    private static MyObject myObject;
    
    
    @BeforeClass
    public static void initializeRegistry() {
        registryStarter = new RegistryStarter();
        Registry registry = new Registry();
        
        myObject = new MyObject();
        registry.addObject("myObject", myObject);
        
        registryStarter.setRegistry(registry);
        registryStarter.startRegistry();
    }
    
    @AfterClass
    public static void stopRegistry() {
        registryStarter.stopRegistry();
    }
    
    @Test
    public void shouldReturnListOfAllObjectNames() throws InterruptedException {
        System.out.println("Check ");
        Thread.sleep(100000);
    }
}
