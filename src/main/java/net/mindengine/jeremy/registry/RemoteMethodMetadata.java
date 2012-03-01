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
