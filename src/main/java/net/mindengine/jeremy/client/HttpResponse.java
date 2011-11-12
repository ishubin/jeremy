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
package net.mindengine.jeremy.client;

import java.util.Map;

public class HttpResponse {
    
    private String url;
    private int status;
    private String content;
    private String language;
    private Map<String, String> headers;
    
    private byte[] bytes;
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
