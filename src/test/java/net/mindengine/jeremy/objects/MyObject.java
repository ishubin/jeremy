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
package net.mindengine.jeremy.objects;

import java.io.File;


public class MyObject implements MyAnotherRemoteInterface, MyRemoteInterface{

    private Long id = 1L;
    private String name = "name";
    private SerialObject serialObject;
    private File file;
    
    
    @Override
    public SerialObject getSerialObject() {
        return serialObject;
    }
    
    @Override
    public void setSerialObject(SerialObject serialObject) {
        this.serialObject = serialObject; 
    }
    
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setLong(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void uploadFile(File file) {
        this.file = file;
    }

    @Override
    public File downloadFile() {
        return this.file;
    }
    
    @Override
    public void someTestMethod() {
        throw new NullPointerException("Some test message");
    }
    
    @Override
    public SuperSample sendSample(SuperSample sample) {
        return sample;
    }
}
