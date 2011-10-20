package net.mindengine.jeremy.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import net.mindengine.jeremy.exceptions.DeserializationException;
import net.mindengine.jeremy.messaging.RequestResponseHandler;
import net.mindengine.jeremy.messaging.json.DefaultJsonRequestResponseHandler;
import net.mindengine.jeremy.tests.mocks.HttpRequestMock;

import org.junit.Test;

public class RequestResponseHandlerUnitTest {

    
    public Long sampleMethod(String arg1, Integer arg2, List<Integer>args) {
        return 123L;
    }
    
    
    @SuppressWarnings("unchecked")
    @Test
    public void jsonRequestResponseUnitTest() throws SecurityException, NoSuchMethodException, DeserializationException {
        RequestResponseHandler handler = new DefaultJsonRequestResponseHandler();
        
        HttpServletRequest request = new HttpRequestMock("[\"string text\", 12, [2,3]]");
        
        Method method = getClass().getMethod("sampleMethod", String.class, Integer.class, List.class);
        Object[]objects = handler.getObjects(method, request);
        
        assertNotNull(objects);
        assertEquals(3, objects.length);
        
        assertEquals("string text", (String)objects[0]);
        assertEquals((Integer)12, (Integer)objects[1]);
        List<Integer> ints = (List<Integer>)objects[2];
        
        assertEquals(2, ints.size());
        assertEquals(2, (int)ints.get(0));
        assertEquals(3, (int)ints.get(1));
    }
}
