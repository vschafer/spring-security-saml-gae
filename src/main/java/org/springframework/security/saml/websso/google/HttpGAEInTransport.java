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

import com.google.appengine.api.urlfetch.HTTPResponse;
import org.opensaml.ws.transport.http.HTTPInTransport;
import org.opensaml.ws.transport.http.HTTPTransport;
import org.opensaml.ws.transport.http.LocationAwareInTransport;
import org.opensaml.xml.security.credential.Credential;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Class wraps GAE's HTTPResponse object into OpenSAML specific transport object.
 */
public class HttpGAEInTransport implements HTTPInTransport, LocationAwareInTransport {

    private final HTTPResponse response;
    private final String endpointURI;

    public HttpGAEInTransport(HTTPResponse response, String endpointURI) {
        this.endpointURI = endpointURI;
        this.response = response;
    }

    public String getLocalAddress() {
        return this.endpointURI;
    }

    public String getPeerAddress() {
        return null;
    }

    public String getPeerDomainName() {
        return null;
    }

    public InputStream getIncomingStream() {
        return new ByteArrayInputStream(this.response.getContent());
    }

    public Object getAttribute(String s) {
        return null;
    }

    public String getCharacterEncoding() {
        return "UTF-8";
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
        return "POST";
    }

    public int getStatusCode() {
        return this.response.getResponseCode();
    }

    public String getParameterValue(String s) {
        return null;
    }

    public List<String> getParameterValues(String s) {
        return null;
    }

    public HTTPTransport.HTTP_VERSION getVersion() {
        return null;
    }

}
