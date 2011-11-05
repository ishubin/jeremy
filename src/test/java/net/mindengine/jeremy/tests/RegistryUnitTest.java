/*******************************************************************************
 * 2011 Ivan Shubin http://mindengine.net
 * 
 * This file is part of Mind-Engine Jeremy.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Mind-Engine Jeremy.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
