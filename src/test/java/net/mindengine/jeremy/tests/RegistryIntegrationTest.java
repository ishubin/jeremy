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
package net.mindengine.jeremy.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.mindengine.jeremy.bin.RemoteFile;
import net.mindengine.jeremy.client.Client;
import net.mindengine.jeremy.client.HttpResponse;
import net.mindengine.jeremy.exceptions.ConnectionError;
import net.mindengine.jeremy.exceptions.RemoteObjectIsNotFoundException;
import net.mindengine.jeremy.messaging.binary.DefaultBinaryLanguageHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonLanguageHandler;
import net.mindengine.jeremy.objects.MyAnotherRemoteInterface;
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
        registry.addLanguageHandler(Client.LANGUAGE_JSON, new DefaultJsonLanguageHandler());
        registry.setDefaultLanguage(Client.LANGUAGE_JSON);
        
        myObject = new MyObject();
        registry.addObject("myObject", myObject);
        registry.addObject("myObject2", new MyObject());
        registry.setPort(8085);
        
        registryStarter.setRegistry(registry);
        registryStarter.startRegistry();
        Thread.sleep(2000);
        
        lookup = new Lookup(url);
        lookup.setDefaultLanguage(Client.LANGUAGE_JSON);
        lookup.addLanguageHandler(Client.LANGUAGE_BINARY, new DefaultBinaryLanguageHandler());
        lookup.addLanguageHandler(Client.LANGUAGE_JSON, new DefaultJsonLanguageHandler());
    }
    
    @AfterClass
    public static void stopRegistry() {
        registryStarter.stopRegistry();
    }
    
    @Test
    public void shouldReturnListOfAllObjectNames() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException {
        Client client = new Client();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Client.LANGUAGE_HEADER, Client.LANGUAGE_JSON);
        HttpResponse response = client.getRequest(url+"/~", null, headers);
        
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
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Client.LANGUAGE_HEADER, Client.LANGUAGE_JSON);
        HttpResponse response = client.getRequest(url+"/myObject/~", null, headers);
        
        assertEquals(200, response.getStatus());
        
        
        ObjectMapper mapper = new ObjectMapper();
        String[] objects = mapper.readValue(response.getContent(), String[].class);
        assertNotNull(objects);
        assertEquals(9, objects.length);
        assertContains(objects, "getName");
        assertContains(objects, "getId");
        assertContains(objects, "setName");
        assertContains(objects, "setLong");
        assertContains(objects, "uploadFile");
        assertContains(objects, "downloadFile");
        assertContains(objects, "setSerialObject");
        assertContains(objects, "getSerialObject");
        assertContains(objects, "someTestMethod");
    }
    
    @Test
    public void shouldReturnRemoteMethodMetadata1() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException, IOException {
        Client client = new Client();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Client.LANGUAGE_HEADER, Client.LANGUAGE_JSON);
        HttpResponse response = client.getRequest(url+"/myObject/setName/~", null, headers);
        
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
        RemoteFile remoteFile = new RemoteFile(new File(getClass().getResource("/test-file.png").toURI()));
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutputStream out = new ObjectOutputStream(bos) ;
        out.writeObject(remoteFile);
        out.close();
        
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Client.LANGUAGE_HEADER, Client.LANGUAGE_JSON);
        HttpResponse response = client.sendMultiPartBinaryRequest("http://localhost:8085/~bin", "myCustomParamName", new ByteArrayInputStream(bos.toByteArray()), headers);
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
    
    @Test
    public void shouldSendNullObjectAlongWithRemoteMethodInvocation() throws RemoteObjectIsNotFoundException, ConnectionError {
      //Creating proxy for remote object
        MyRemoteInterface remoteInterface = lookup.getRemoteObject("myObject", MyRemoteInterface.class);
        
        //Invoking remote method.
        remoteInterface.setSerialObject(null);
        
        SerialObject obj = remoteInterface.getSerialObject();
        
        assertNull(obj);
    }
    
    
    @Test
    public void shouldSendBinaryObjectAlongWithRemoteMethodInvocation() throws IOException, URISyntaxException, RemoteObjectIsNotFoundException, ConnectionError {
      //Creating proxy for remote object
        MyAnotherRemoteInterface remoteInterface = lookup.getRemoteObject("myObject", MyAnotherRemoteInterface.class);
        
        //Invoking remote method.
        RemoteFile file = new RemoteFile(new File(getClass().getResource("/test-file.png").toURI()));
        
        assertNotNull(file.getBytes());
        assertTrue(file.getBytes().length>0);
        
        remoteInterface.uploadFile(file);
        
        RemoteFile file2 = remoteInterface.downloadFile();
        assertNotNull(file2);
        assertNotNull(file2.getBytes());
        
        assertEquals(file.getBytes().length, file2.getBytes().length);
        
        for(int i=0;i<file.getBytes().length; i++) {
            assertEquals(file.getBytes()[i], file2.getBytes()[i]);
        }
    }
    
    @Test
    public void remoteMethodShouldBeAbleToThrowExceptions() throws RemoteObjectIsNotFoundException, ConnectionError {
      //Creating proxy for remote object
        MyRemoteInterface remoteInterface = lookup.getRemoteObject("myObject", MyRemoteInterface.class);
        
        Exception exception = null;
        try {
            remoteInterface.someTestMethod();
        }
        catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(NullPointerException.class, exception.getClass());
        assertEquals("Some test message", exception.getMessage());
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
