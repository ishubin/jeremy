package net.mindengine.jeremy.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.exceptions.SerializationException;
import net.mindengine.jeremy.messaging.RequestResponseHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonRequestResponseHandler;
import net.mindengine.jeremy.tests.mocks.HttpRequestMock;

import org.junit.Ignore;
import org.junit.Test;

public class RequestResponseHandlerUnitTest {

    
    public Long sampleMethod(String arg1, Integer arg2, Integer[]arg3) {
        return 123L;
    }
    
    
    @Test
    public void deserializationForMultipleSimpleMethodArgumentsShouldWork() throws SecurityException, NoSuchMethodException, DeserializationException {
        RequestResponseHandler handler = new DefaultJsonRequestResponseHandler();
        
        HttpServletRequest request = new HttpRequestMock();
        ((HttpRequestMock)request).putParameter("arg0", "\"string text\"");
        ((HttpRequestMock)request).putParameter("arg1", "12");
        ((HttpRequestMock)request).putParameter("arg2", "[2,3]");
        
        Method method = getClass().getMethod("sampleMethod", String.class, Integer.class, Integer[].class);
        Object[]objects = handler.getObjects(method, request);
        
        assertNotNull(objects);
        assertEquals(3, objects.length);
        
        assertEquals("string text", (String)objects[0]);
        assertEquals((Integer)12, (Integer)objects[1]);
        Integer[] ints = (Integer[])objects[2];
        
        assertEquals(2, ints.length);
        assertEquals(2, (int)ints[0]);
        assertEquals(3, (int)ints[1]);
        
    }
    
    @Test
    public void serializationForSimpleMethodArgumentsShouldWork() throws SerializationException {
        RequestResponseHandler handler = new DefaultJsonRequestResponseHandler();
        
        assertEquals("\"string text\"", handler.serializeResponse("string text"));
        assertEquals("132", handler.serializeResponse((Integer)132));
        assertEquals("[3,4,1]", handler.serializeResponse(new Integer[]{3,4,1}));
    }
    
    @Ignore
    @Test
    public void deserializationForComplexObjectShouldWork() {
        //TODO unit test
    }
    
    @Ignore
    @Test
    public void serializationForComplexObjectShouldWork() {
        //TODO unit test
    }
}
