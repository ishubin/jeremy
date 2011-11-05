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
package net.mindengine.jeremy.objects;

import net.mindengine.jeremy.bin.RemoteFile;


public class MyObject implements MyAnotherRemoteInterface, MyRemoteInterface{

    private Long id = 1L;
    private String name = "name";
    private SerialObject serialObject;
    private RemoteFile file;
    
    
    
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
    public void uploadFile(RemoteFile file) {
        this.file = file;
    }

    @Override
    public RemoteFile downloadFile() {
        return this.file;
    }
    
    @Override
    public void someTestMethod() {
        throw new NullPointerException("Some test message");
    }
}
