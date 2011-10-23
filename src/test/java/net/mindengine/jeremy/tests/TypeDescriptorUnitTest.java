package net.mindengine.jeremy.tests;

import java.io.IOException;

import net.mindengine.jeremy.objects.SerialObject;
import net.mindengine.jeremy.registry.TypeDescriptor;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class TypeDescriptorUnitTest {

    
    @Test
    public void checkThatTypeDescriptorCollectAllMetadata() throws JsonGenerationException, JsonMappingException, IOException {
        TypeDescriptor descr = TypeDescriptor.createDescriptor(SerialObject.class);
        //TODO finish test
        ObjectMapper mapper = new ObjectMapper();
        
        System.out.println(mapper.writeValueAsString(descr));
    }
}
