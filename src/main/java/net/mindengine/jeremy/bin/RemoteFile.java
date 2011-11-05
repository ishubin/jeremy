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
