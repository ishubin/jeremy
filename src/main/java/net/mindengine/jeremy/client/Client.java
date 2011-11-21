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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class Client {
    
    private int maxBufferSize = 1024;
    public static final String LANGUAGE_BINARY = "binary".intern();
    public static final String LANGUAGE_JSON = "json".intern();
    public static final String LANGUAGE_HEADER = "x-language".intern();
    public static final String CLASS_PATH = "x-class-path".intern();
    /**
     * Send GET or POST request
     * @param targetUrl
     * @param httpParams
     * @param post If true - POST request will be sent, otherwise - GET
     * @return
     */
    private HttpResponse sendRequest(String targetUrl, Map<String, String> httpParams, boolean post, Map<String, String> httpHeaders)throws KeyManagementException, NoSuchAlgorithmException, IOException  {
        if(targetUrl.startsWith("https")) {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
            SSLContext.setDefault(ctx);
        }

        URL url = new URL(targetUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            httpsConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        }

        if(post) {
            connection.setRequestMethod("POST");
        }
        else connection.setRequestMethod("GET");
        connection.setRequestMethod("GET");
        
        
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Language", "en-US");
        
        if(httpHeaders!=null) {
            for(Map.Entry<String, String>header : httpHeaders.entrySet()){
                connection.addRequestProperty(header.getKey(), header.getValue());
            }
        }
        
        connection.setDoInput(true);

        // Send request
        if(post && httpParams!=null && httpParams.size() > 0){
            String urlParameters = "";
            
            boolean amp = false;
            for(Map.Entry<String, String> httpParam : httpParams.entrySet()) {
                if(amp) {
                    urlParameters+="&";
                }
                urlParameters+= httpParam.getKey()+"="+URLEncoder.encode(httpParam.getValue(), "UTF-8");
                amp = true;
            }
            
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();
        }

        //Read response
        InputStream is;
        if (connection.getResponseCode() >= 400) {
            is = connection.getErrorStream();
        }
        else {
            is = connection.getInputStream();
        }
        
        HttpResponse response = new HttpResponse();
        response.setUrl(targetUrl);
        response.setStatus(connection.getResponseCode());
        response.setHeaders(collectResponseHeaders(connection));
        response.setLanguage(response.getHeaders().get(Client.LANGUAGE_HEADER));
        
        
        String responseLanguage = connection.getHeaderField(Client.LANGUAGE_HEADER);
        
        if(LANGUAGE_BINARY.equals(responseLanguage)) {
            // Reading binary response
            ByteArrayOutputStream bous = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ( (len1 = is.read(buffer)) > 0 ) {
                bous.write(buffer,0, len1);
            }
            bous.close();
            response.setBytes(bous.toByteArray());
        }
        else {
            // Reading text response
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer buff = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                buff.append(line);
                buff.append('\r');
            }
            rd.close();
            response.setContent(buff.toString());
        }

        return response;
    }
    
    private void writeBytes(OutputStream os, String text) throws IOException {
        os.write(text.getBytes());
    }
    
    public HttpResponse sendMultiPartBinaryRequest(String targetUrl, String name, InputStream inputStream, Map<String, String> httpHeaders) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        if(targetUrl.startsWith("https")) {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
            SSLContext.setDefault(ctx);
        }

        URL url = new URL(targetUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            httpsConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        }

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        connection.setRequestMethod("POST");
        
        if(httpHeaders!=null) {
            for(Map.Entry<String, String> header : httpHeaders.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }
        
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setDoInput(true);

     // Send request
        OutputStream os = connection.getOutputStream();

        writeBytes(os, twoHyphens + boundary + lineEnd);
        writeBytes(os, "Content-Disposition: form-data; name=\"" + name + "\";" + " filename=\"" + name + "\"" + lineEnd);
        writeBytes(os, lineEnd);

        int bytesAvailable = inputStream.available();
        int bufferSize = Math.min(bytesAvailable, this.maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = inputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            os.write(buffer, 0, bufferSize);
            bytesAvailable = inputStream.available();
            bufferSize = Math.min(bytesAvailable, this.maxBufferSize);

            bytesRead = inputStream.read(buffer, 0, bufferSize);
        }

        writeBytes(os, lineEnd);
        writeBytes(os, twoHyphens + boundary + twoHyphens + lineEnd);

        inputStream.close();

        os.flush();
        os.close();

        InputStream is;
        if (connection.getResponseCode() >= 400) {
            is = connection.getErrorStream();
        }
        else {
            is = connection.getInputStream();
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer buff = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            buff.append(line);
            buff.append('\r');
        }
        rd.close();

        HttpResponse response = new HttpResponse();
        response.setContent(buff.toString());
        response.setStatus(connection.getResponseCode());
        response.setHeaders(collectResponseHeaders(connection));
        response.setLanguage(response.getHeaders().get(Client.LANGUAGE_HEADER));
        return response;
    }
    
    private Map<String, String> collectResponseHeaders(HttpURLConnection connection) {
        Map<String, String> headers = new HashMap<String, String>();
        for (int i=0;i<10000; i++) {
            String headerName = connection.getHeaderFieldKey(i);
            String headerValue = connection.getHeaderField(i);
            
            if (headerName == null && headerValue == null) {
                // No more headers
                break;
            }
            if (headerName != null) {
                headers.put(headerName, headerValue);
            }
        }
        return headers;
    }

    public HttpResponse postRequest(String targetUrl, Map<String, String> httpParams, Map<String, String> httpHeaders) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        return sendRequest(targetUrl, httpParams, true, httpHeaders);
    }
    
    public HttpResponse getRequest(String targetUrl, Map<String, String> httpParams, Map<String, String> httpHeaders) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        if(httpParams!=null) {
            if(targetUrl.contains("?")) {
                targetUrl+="&";
            }
            
            
            boolean amp = false;
            for(Map.Entry<String, String> httpParam : httpParams.entrySet()) {
                if(amp) {
                    targetUrl+="&";
                }
                targetUrl+= httpParam.getKey()+"="+URLEncoder.encode(httpParam.getValue(), "UTF-8");
                amp = true;
            }
        }
        return sendRequest(targetUrl, httpParams, false, httpHeaders);
    }
    
    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
    
    
    public void setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

}
