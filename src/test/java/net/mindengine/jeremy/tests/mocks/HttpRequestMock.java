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
package net.mindengine.jeremy.tests.mocks;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

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

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest,
			ServletResponse servletResponse) throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean authenticate(HttpServletResponse response)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void login(String username, String password) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		// TODO Auto-generated method stub
		return null;
	}

}
