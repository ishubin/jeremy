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
package net.mindengine.jeremy.exceptions;

public class SerializationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -7864071920207162027L;

    public SerializationException() {
        super();
    }

    public SerializationException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public SerializationException(String paramString) {
        super(paramString);
    }

    public SerializationException(Throwable paramThrowable) {
        super(paramThrowable);
    }

    
}
