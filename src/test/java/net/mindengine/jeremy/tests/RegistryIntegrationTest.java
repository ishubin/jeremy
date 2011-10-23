package net.mindengine.jeremy.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.objects.MyObject;
import net.mindengine.jeremy.registry.Registry;
import net.mindengine.jeremy.starter.RegistryStarter;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegistryIntegrationTest {

    
    private static RegistryStarter registryStarter;
    private static MyObject myObject;
    
    
    @BeforeClass
    public static void initializeRegistry() throws InterruptedException {
        registryStarter = new RegistryStarter();
        Registry registry = new Registry();
        
        myObject = new MyObject();
        registry.addObject("myObject", myObject);
        registry.addObject("myObject2", new MyObject());
        
        registryStarter.setRegistry(registry);
        registryStarter.startRegistry();
        Thread.sleep(2000);
    }
    
    @AfterClass
    public static void stopRegistry() {
        registryStarter.stopRegistry();
    }
    
    @Test
    public void shouldReturnListOfAllObjectNames() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException {
        Client client = new Client();
        HttpResponse response = client.getRequest("http://localhost:8085/~", null);
        
        assertEquals(200, response.getStatus());
        
        
        ObjectMapper mapper = new ObjectMapper();
        String[] objects = mapper.readValue(response.getContent(), String[].class);
        assertNotNull(objects);
        assertEquals(2, objects.length);
        assertContains(objects, "myObject");
        assertContains(objects, "myObject2");
        
    }
    
    @Test
    public void shouldReturnListOfAllMethodsPerObject() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException {
        Client client = new Client();
        HttpResponse response = client.getRequest("http://localhost:8085/myObject/~", null);
        
        assertEquals(200, response.getStatus());
        
        
        ObjectMapper mapper = new ObjectMapper();
        String[] objects = mapper.readValue(response.getContent(), String[].class);
        assertNotNull(objects);
        assertEquals(4, objects.length);
        assertContains(objects, "getName");
        assertContains(objects, "getId");
        assertContains(objects, "setName");
        assertContains(objects, "setLong");
        
    }
    
    private static void assertContains(Object[]array, Object value) {
        for(Object item : array) {
            if(item.equals(value)) {
                return;
            }
        }
        throw new RuntimeException("Cannot find item "+value+":\n"+arrayToString(array));
    }
    
    private static String arrayToString(Object[]array) {
        String str = "[";
        for(int i=0; i<array.length; i++) {
            if(i>0)str+=",";
            str+=array[i];
        }
        str+="]";
        return str;
    }
    
    
    
}
