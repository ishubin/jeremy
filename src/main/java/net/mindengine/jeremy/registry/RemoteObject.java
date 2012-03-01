/*******************************************************************************
* Copyright 2012 Ivan Shubin http://mindengine.net
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
package net.mindengine.jeremy.registry;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteObject {

    private String name;
    private Object object;
    private Map<String, Method> remoteMethods = new ConcurrentHashMap<String, Method>();
    public void setObject(Object object) {
        this.object = object;
    }
    public Object getObject() {
        return object;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setRemoteMethods(Map<String, Method> remoteMethods) {
        this.remoteMethods = remoteMethods;
    }
    public Map<String, Method> getRemoteMethods() {
        return remoteMethods;
    }
}
