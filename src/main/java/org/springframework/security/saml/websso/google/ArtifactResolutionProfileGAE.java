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

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.FetchOptions.Builder;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

import java.io.IOException;
import java.net.URL;

import org.opensaml.common.SAMLException;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.security.SecurityException;
import org.slf4j.Logger;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.saml.websso.ArtifactResolutionProfileBase;

/**
 * Custom implementation of Artifact resolution which perform back-end SOAP call using GAE's specific classes.
 */
public class ArtifactResolutionProfileGAE extends ArtifactResolutionProfileBase {

    protected void getArtifactResponse(String endpointURI, SAMLMessageContext context)
            throws SAMLException, MessageEncodingException, MessageDecodingException, MetadataProviderException, SecurityException {

        try {

            URL url = new URL(context.getPeerEntityEndpoint().getLocation());
            URLFetchService urlService = URLFetchServiceFactory.getURLFetchService();
            HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST, FetchOptions.Builder.disallowTruncate().doNotFollowRedirects().doNotValidateCertificate());

            HttpGAEOutTransport clientOutTransport = new HttpGAEOutTransport(request);
            context.setOutboundMessageTransport(clientOutTransport);

            boolean signMessage = context.getPeerExtendedMetadata().isRequireArtifactResolveSigned();
            this.processor.sendMessage(context, signMessage, "urn:oasis:names:tc:SAML:2.0:bindings:SOAP");

            clientOutTransport.flush();

            HTTPResponse response = urlService.fetch(request);
            int responseCode = response.getResponseCode();

            if (responseCode != 200) {
                log.debug("Problem communicating with Artifact Resolution service, received response {}.", responseCode);
                throw new MessageDecodingException("Problem communicating with Artifact Resolution service, received response " + responseCode);
            }

            HttpGAEInTransport clientInTransport = new HttpGAEInTransport(response, endpointURI);
            context.setInboundMessageTransport(clientInTransport);

            this.processor.retrieveMessage(context, "urn:oasis:names:tc:SAML:2.0:bindings:SOAP");

        } catch (IOException e) {
            log.debug("Error when sending request to artifact resolution service.", e);
            throw new MessageDecodingException("Error when sending request to artifact resolution service.", e);
        }

    }

}
