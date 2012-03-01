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
package net.mindengine.jeremy.starter;

import net.mindengine.jeremy.registry.Registry;

public class RegistryStarter extends Thread {

    private Registry registry;
    
    @Override
    public void run() {
        try {
            registry.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void startRegistry() {
        this.start();
    }
    
    
    public void stopRegistry() {
        try {
            registry.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setRegistry(Registry registry) {
        this.registry = registry;
    }


    public Registry getRegistry() {
        return registry;
    }
}
