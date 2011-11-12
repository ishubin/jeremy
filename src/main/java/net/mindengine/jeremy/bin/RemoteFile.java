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
package net.mindengine.jeremy.bin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.io.FileUtils;

/**
 * Used when it is needed to send the file contents to a remote server
 * @author Ivan Shubin
 *
 */
public class RemoteFile implements Binary, Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -1800931527219318420L;
    private String name;
    private byte[]bytes;
    
    
    public RemoteFile() {
    }
    
    public RemoteFile(File file) throws IOException {
        loadFile(file);
    }

    private void loadFile(File file) throws IOException {
        this.setName(file.getName());
        this.setBytes(FileUtils.readFileToByteArray(file));
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
