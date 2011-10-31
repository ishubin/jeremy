package net.mindengine.jeremy.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialArray;

import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.objects.MyObject;
import net.mindengine.jeremy.objects.MyRemoteInterface;
import net.mindengine.jeremy.objects.SerialObject;
import net.mindengine.jeremy.registry.Lookup;
import net.mindengine.jeremy.registry.Registry;
import net.mindengine.jeremy.registry.RemoteMethodMetadata;
import net.mindengine.jeremy.starter.RegistryStarter;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegistryIntegrationTest {

    
    private static RegistryStarter registryStarter;
    private static MyObject myObject;
    
    private static final String url = "http://localhost:8085";
    
    private static Lookup lookup = new Lookup(url);
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
        
        lookup = new Lookup(url);
        
    }
    
    @AfterClass
    public static void stopRegistry() {
        registryStarter.stopRegistry();
    }
    
    @Test
    public void shouldReturnListOfAllObjectNames() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException {
        Client client = new Client();
        HttpResponse response = client.getRequest(url+"/~", null);
        
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
        HttpResponse response = client.getRequest(url+"/myObject/~", null);
        
        assertEquals(200, response.getStatus());
        
        
        ObjectMapper mapper = new ObjectMapper();
        String[] objects = mapper.readValue(response.getContent(), String[].class);
        assertNotNull(objects);
        assertEquals(6, objects.length);
        assertContains(objects, "getName");
        assertContains(objects, "getId");
        assertContains(objects, "setName");
        assertContains(objects, "setLong");
        assertContains(objects, "setSerialObject");
        assertContains(objects, "getSerialObject");
    }
    
    @Test
    public void shouldReturnRemoteMethodMetadata1() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException {
        Client client = new Client();
        HttpResponse response = client.getRequest(url+"/myObject/setName/~", null);
        
        assertEquals(200, response.getStatus());
        
        ObjectMapper mapper = new ObjectMapper();
        RemoteMethodMetadata md = mapper.readValue(response.getContent(), RemoteMethodMetadata.class);
        assertNotNull(md);
        assertEquals(1, md.getArguments().size());
        assertNull(md.getArguments().get(0).getFields());
        assertEquals(String.class.getName(), md.getArguments().get(0).getType());
        assertNull(md.getReturns());
        assertEquals("setName", md.getMethod());
        assertEquals("myObject", md.getObject());
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void uploadFileShouldReturnIdOfObjectsInCache() throws URISyntaxException, KeyManagementException, NoSuchAlgorithmException, IOException {
        Client client = new Client();
        
        InputStream inputStream = getClass().getResourceAsStream("/test-file.png");
        assertNotNull(inputStream);
        HttpResponse response = client.sendMultiPartBinaryRequest("http://localhost:8085/~file", "myCustomParamName", inputStream);
        ObjectMapper mapper = new ObjectMapper();
        String content = response.getContent();
        
        assertEquals(200, response.getStatus());
        Map<String, String> map = (Map<String, String>) mapper.readValue(content, new HashMap<String, String>().getClass());
        
        assertNotNull(map);
        assertEquals(1, map.size());
        assertNotNull(map.get("myCustomParamName"));
        assertTrue(!map.get("myCustomParamName").isEmpty());
    }
    
    @Test
    public void shouldInvokeRemoteMethods() throws RemoteObjectIsNotFoundException, ConnectionError {
        
        //Creating proxy for remote object
        MyRemoteInterface remoteInterface = lookup.getRemoteObject("myObject", MyRemoteInterface.class);
        
        //Invoking remote method.
        remoteInterface.setName("qwe");
        
        //Checking that the previous method was implemented properly 
        String name = remoteInterface.getName();
        assertEquals("qwe", name);
    }
    
    @Test
    public void shouldSendComplexObjectAlongWithRemoteMethodInvocation() throws RemoteObjectIsNotFoundException, ConnectionError {
      //Creating proxy for remote object
        MyRemoteInterface remoteInterface = lookup.getRemoteObject("myObject", MyRemoteInterface.class);
        
        //Invoking remote method.
        SerialObject object = new SerialObject();
        object.setDate(new Date(12345678));
        object.setDoubleField(34.056);
        object.setLongField(234L);
        object.setFloatField(12.03f);
        object.setStringField("test object");
        remoteInterface.setSerialObject(object);
        
        
        SerialObject obj2 = remoteInterface.getSerialObject();
        
        assertNotNull(obj2);
        assertEquals(object.getDate(), obj2.getDate());
        assertEquals(object.getDoubleField(), obj2.getDoubleField());
        assertEquals(object.getLongField(), obj2.getLongField());
        assertEquals(object.getFloatField(), obj2.getFloatField());
        assertEquals(object.getStringField(), obj2.getStringField());
        
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
