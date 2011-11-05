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
package net.mindengine.jeremy.tests.mocks;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpRequestMock implements HttpServletRequest{

    private String requestBody;
    private Map<String, String> parameters = new HashMap<String, String>();
    
    public HttpRequestMock() {
        
    }
    
    public HttpRequestMock(String requestBody) {
        this.requestBody = requestBody;
    }
    
    public void putParameter(String name, String value) {
        parameters.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        
    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        
        final byte[] bytes = getRequestBody().getBytes();
        
        ServletInputStream stream = new ServletInputStream() {
            
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
        
        return stream;
    }

    @Override
    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getParameterNames() {
        
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map getParameterMap() {
        
        return null;
    }

    @Override
    public String getProtocol() {
        
        return null;
    }

    @Override
    public String getScheme() {
        
        return null;
    }

    @Override
    public String getServerName() {
        
        return null;
    }

    @Override
    public int getServerPort() {
        
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        
        return null;
    }

    @Override
    public String getRemoteAddr() {
        
        return null;
    }

    @Override
    public String getRemoteHost() {
        
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {
        
        
    }

    @Override
    public void removeAttribute(String name) {
        
        
    }

    @Override
    public Locale getLocale() {
        
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getLocales() {
        
        return null;
    }

    @Override
    public boolean isSecure() {
        
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        
        return null;
    }

    @Override
    public String getRealPath(String path) {
        
        return null;
    }

    @Override
    public int getRemotePort() {
        
        return 0;
    }

    @Override
    public String getLocalName() {
        
        return null;
    }

    @Override
    public String getLocalAddr() {
        
        return null;
    }

    @Override
    public int getLocalPort() {
        
        return 0;
    }

    @Override
    public String getAuthType() {
        
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        
        return null;
    }

    @Override
    public long getDateHeader(String name) {
        
        return 0;
    }

    @Override
    public String getHeader(String name) {
        
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getHeaders(String name) {
        
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getHeaderNames() {
        
        return null;
    }

    @Override
    public int getIntHeader(String name) {
        
        return 0;
    }

    @Override
    public String getMethod() {
        
        return null;
    }

    @Override
    public String getPathInfo() {
        
        return null;
    }

    @Override
    public String getPathTranslated() {
        
        return null;
    }

    @Override
    public String getContextPath() {
        
        return null;
    }

    @Override
    public String getQueryString() {
        
        return null;
    }

    @Override
    public String getRemoteUser() {
        
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        
        return null;
    }

    @Override
    public String getRequestURI() {
        
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        
        return null;
    }

    @Override
    public String getServletPath() {
        
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        
        return null;
    }

    @Override
    public HttpSession getSession() {
        
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        
        return false;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

}
