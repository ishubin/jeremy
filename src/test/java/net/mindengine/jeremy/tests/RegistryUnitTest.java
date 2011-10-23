package net.mindengine.jeremy.tests;

import net.mindengine.jeremy.Remote;
import net.mindengine.jeremy.registry.Registry;

import org.junit.Test;

public class RegistryUnitTest {
    
    public class RemoteObjectWithNoMethods implements Remote {
        public void myMethod1() {
            
        }
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void objectWithoutRemoteMethodsShouldThrowException() {
        Registry registry = new Registry();
        registry.addObject("object1", new RemoteObjectWithNoMethods());
    }

}
