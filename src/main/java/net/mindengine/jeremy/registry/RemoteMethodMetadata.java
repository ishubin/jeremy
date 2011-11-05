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
package net.mindengine.jeremy.registry;

import java.io.Serializable;
import java.util.List;

public class RemoteMethodMetadata implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6295260579836788636L;
    private String method;
    private String object;
    private List<TypeDescriptor> arguments;
    private TypeDescriptor returns;
    public void setReturns(TypeDescriptor returns) {
        this.returns = returns;
    }
    public TypeDescriptor getReturns() {
        return returns;
    }
    public void setArguments(List<TypeDescriptor> arguments) {
        this.arguments = arguments;
    }
    public List<TypeDescriptor> getArguments() {
        return arguments;
    }
    public void setObject(String object) {
        this.object = object;
    }
    public String getObject() {
        return object;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return method;
    }
}
