package net.mindengine.jeremy.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.LanguageHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonLanguageHandler;
import net.mindengine.jeremy.objects.SerialObject;
import net.mindengine.jeremy.tests.mocks.HttpRequestMock;

import org.junit.Test;

public class RequestResponseHandlerUnitTest {

    
    public Long sampleMethod(String arg1, Integer arg2, Integer[]arg3) {
        return 123L;
    }
    
    
    @Test
    public void jsonSerializationForSimpleMethodArgumentsShouldWork() throws SerializationException {
        LanguageHandler handler = new DefaultJsonLanguageHandler();
        
        assertEquals("\"string text\"", handler.serializeResponse("string text"));
        assertEquals("132", handler.serializeResponse((Integer)132));
        assertEquals("[3,4,1]", handler.serializeResponse(new Integer[]{3,4,1}));
    }
    
    /**
     * Just a sample method so it could be used in unit test for testing deserialization
     */
    public void testMethod(SerialObject object) {
        
    }
    
    @Test
    public void jsonSerializationDeserializationForComplexObjectShouldWork() throws SerializationException, IOException, URISyntaxException, SecurityException, DeserializationException, NoSuchMethodException {
        SerialObject obj = new SerialObject();
        Date date = new Date(1319180501953L);
        obj.setDate(date);
        obj.setDoubleField(12.3);
        obj.setFloatField(23.1f);
        
        List<SerialObject> list =new LinkedList<SerialObject>();
        list.add(new SerialObject("obj1"));
        list.add(new SerialObject("obj2"));
        list.add(new SerialObject("obj3"));
        obj.setListOfObjects(list);
        
        obj.setLongField(45L);
        
        Map<String, SerialObject> map = new HashMap<String, SerialObject>();
        map.put("obj1", new SerialObject("map_object1"));
        map.put("obj2", new SerialObject("map_object2"));
        obj.setMapOfObjects(map);
        
        obj.setNestedObject(new SerialObject("Nested object"));
        obj.setStringField("object");
        LanguageHandler handler = new DefaultJsonLanguageHandler();
        String serialized = handler.serializeResponse(obj);
        
        HttpServletRequest request = new HttpRequestMock();
        ((HttpRequestMock)request).putParameter("arg0", serialized);
        
        SerialObject objDes = (SerialObject) handler.deserializeObject(serialized, SerialObject.class);
        
        assertNotNull(objDes);
        
        assertEquals(1319180501953L, objDes.getDate().getTime());
        assertEquals((Double)12.3, objDes.getDoubleField());
        assertEquals((Float)23.1f, objDes.getFloatField());
        assertEquals((int)3, objDes.getListOfObjects().size());
        assertEquals("obj1", objDes.getListOfObjects().get(0).getStringField());
        assertNull(objDes.getListOfObjects().get(0).getDoubleField());
        assertEquals("obj2", objDes.getListOfObjects().get(1).getStringField());
        assertNull(objDes.getListOfObjects().get(1).getDoubleField());
        assertEquals("obj3", objDes.getListOfObjects().get(2).getStringField());
        assertNull(objDes.getListOfObjects().get(2).getDoubleField());
        
        assertEquals((Long)45L, objDes.getLongField());
        
        assertEquals("map_object1", objDes.getMapOfObjects().get("obj1").getStringField());
        assertEquals("map_object2", objDes.getMapOfObjects().get("obj2").getStringField());
        
        assertEquals("Nested object", objDes.getNestedObject().getStringField());
        assertEquals("object", objDes.getStringField());
    }
}
