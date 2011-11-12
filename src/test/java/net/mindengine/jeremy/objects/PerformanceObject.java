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
package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.bin.RemoteFile;

public class PerformanceObject implements PerformanceObjectInterface{

    private int executeSimpleMethodCount = 0;
    private int executeMethodWithBinaries = 0;
    
    @Override
    public void executeSimpleMethod() throws InterruptedException {
        executeSimpleMethodCount++;
        Thread.sleep(5000);
    }

    @Override
    public void executeMethodWithBinaries(RemoteFile file) throws InterruptedException {
        if(file.getBytes()==null) throw new NullPointerException("Bytes are null");
        Thread.sleep(5000);
        executeMethodWithBinaries++;
    }

    public void setExecuteSimpleMethodCount(int executeSimpleMethodCount) {
        this.executeSimpleMethodCount = executeSimpleMethodCount;
    }

    public int getExecuteSimpleMethodCount() {
        return executeSimpleMethodCount;
    }

    public int getExecuteMethodWithBinaries() {
        return executeMethodWithBinaries;
    }

    public void setExecuteMethodWithBinaries(int executeMethodWithBinaries) {
        this.executeMethodWithBinaries = executeMethodWithBinaries;
    }

}
