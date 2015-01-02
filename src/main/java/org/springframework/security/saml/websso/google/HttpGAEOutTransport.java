/* Copyright 2015 Vladimir Schafer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.saml.websso.google;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import org.opensaml.ws.transport.http.HTTPOutTransport;
import org.opensaml.ws.transport.http.HTTPTransport;
import org.opensaml.xml.security.credential.Credential;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Class wraps GAE's HTTPRequest object into OpenSAML specific transport object.
 */
public class HttpGAEOutTransport implements HTTPOutTransport {

    private final ByteArrayOutputStream outputStream;
    private final HTTPRequest request;

    public HttpGAEOutTransport(HTTPRequest request) {
        this.request = request;
        this.outputStream = new ByteArrayOutputStream();
    }

    public void setVersion(HTTPTransport.HTTP_VERSION http_version) {
    }

    public void setHeader(String s, String s1) {
        this.request.addHeader(new HTTPHeader(s, s1));
    }

    public void addParameter(String s, String s1) {
    }

    public void setStatusCode(int i) {
    }

    public void sendRedirect(String s) {
    }

    public void setAttribute(String s, Object o) {
    }

    public void setCharacterEncoding(String s) {
    }

    public OutputStream getOutgoingStream() {
        return this.outputStream;
    }

    public Object getAttribute(String s) {
        return null;
    }

    public String getCharacterEncoding() {
        return "";
    }

    public Credential getLocalCredential() {
        return null;
    }

    public Credential getPeerCredential() {
        return null;
    }

    public boolean isAuthenticated() {
        return false;
    }

    public void setAuthenticated(boolean b) {
    }

    public boolean isConfidential() {
        return false;
    }

    public void setConfidential(boolean b) {
    }

    public boolean isIntegrityProtected() {
        return false;
    }

    public void setIntegrityProtected(boolean b) {
    }

    public String getHeaderValue(String s) {
        return null;
    }

    public String getHTTPMethod() {
        return "";
    }

    public int getStatusCode() {
        return -1;
    }

    public String getParameterValue(String s) {
        return null;
    }

    public List<String> getParameterValues(String s) {
        return null;
    }

    public HTTPTransport.HTTP_VERSION getVersion() {
        return HTTPTransport.HTTP_VERSION.HTTP1_0;
    }

    public void flush() {
        this.request.setPayload(this.outputStream.toByteArray());
    }

}
