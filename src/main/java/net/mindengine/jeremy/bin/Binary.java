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

/**
 * Used as a markup for objects which should be serialized as binary data. For
 * instance in case if it is need to send binary file along with remote method
 * invocation the method argument containing the array of bytes should support
 * this interface.
 * 
 * @author Ivan Shubin
 * 
 */
public interface Binary {

}
