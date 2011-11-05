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

public class PerformanceObjectThread implements PerformanceObjectInterface{

    private PerformanceObjectInterface remoteObject;
    
    @Override
    public void executeSimpleMethod() throws InterruptedException {
        
    }

    @Override
    public void executeMethodWithBinaries(RemoteFile file) throws InterruptedException {
        // TODO Auto-generated method stub
        
    }

    public PerformanceObjectInterface getRemoteObject() {
        return remoteObject;
    }

    public void setRemoteObject(PerformanceObjectInterface remoteObject) {
        this.remoteObject = remoteObject;
    }

}
