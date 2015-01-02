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
package org.springframework.security.saml.metadata.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.opensaml.saml2.metadata.provider.AbstractObservableMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple metadata provider able to load metadata from filesystem. Provider does not start any new threads for reloading
 * of metadata and can be used with platforms which do not support such operations, such as GAE.
 */
public class StaticFilesystemMetadataProvider extends AbstractObservableMetadataProvider {

    private final Logger log = LoggerFactory.getLogger(StaticFilesystemMetadataProvider.class);
    private File metadataFile;

    public StaticFilesystemMetadataProvider(File metadata)
            throws MetadataProviderException {
        setMetadataFile(metadata);
    }

    protected void setMetadataFile(File file)
            throws MetadataProviderException {
        if (!file.exists()) {
            throw new MetadataProviderException("Give metadata file, " + file.getAbsolutePath() + " does not exist");
        }
        if (!file.isFile()) {
            throw new MetadataProviderException("Give metadata file, " + file.getAbsolutePath() + " is not a file");
        }
        if (!file.canRead()) {
            throw new MetadataProviderException("Give metadata file, " + file.getAbsolutePath() + " is not readable");
        }
        this.metadataFile = file;
    }

    protected XMLObject doGetMetadata()
            throws MetadataProviderException {
        try {
            return unmarshallMetadata(inputstreamToByteArray(new FileInputStream(this.metadataFile)));
        } catch (FileNotFoundException e) {
            throw new MetadataProviderException("Error", e);
        }
    }

    protected XMLObject unmarshallMetadata(byte[] metadataBytes)
            throws MetadataProviderException {
        try {
            return unmarshallMetadata(new ByteArrayInputStream(metadataBytes));
        } catch (UnmarshallingException e) {
            String errorMsg = "Unable to unmarshall metadata";
            this.log.error(errorMsg, e);
            throw new MetadataProviderException(errorMsg, e);
        }
    }

    protected byte[] inputstreamToByteArray(InputStream ins)
            throws MetadataProviderException {
        try {
            byte[] buffer = new byte[1048576];
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            long count = 0L;
            int n = 0;
            while (-1 != (n = ins.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
            ins.close();
            return output.toByteArray();
        } catch (IOException e) {
            throw new MetadataProviderException(e);
        }
    }

}
