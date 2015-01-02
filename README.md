# Spring Security SAML GAE

Project enables deployment of Spring SAML applications in Google Application Engine.

## Metadata loading

Provider enables population of SAML meatadata from filesystem. The implementation removes all automated reloading which is not supported by GAE due to limitations on starting of new threads.

**Implementation:** `org.springframework.security.saml.metadata.provider.StaticFilesystemMetadataProvider`

## Artifact resolution

Enables loading of SAML responses using HTTP-Artifact binding using classes available in GAE API.

**Implementation:** `org.springframework.security.saml.websso.google.ArtifactResolutionProfileGAE`

## Usage

In order to install artifact resolution using GAE specific APIs, replace bean `org.springframework.security.saml.websso.ArtifactResolutionProfileImpl` with `org.springframework.security.saml.websso.google.ArtifactResolutionProfileGAE` in your Spring SAML configuration XML.

In order to use metadata loading without reloading threads add a provider to your metadata bean with:

```
<bean class="org.springframework.security.saml.metadata.ExtendedMetadataDelegate">
    <constructor-arg>
        <bean class="org.springframework.security.saml.metadata.provider.StaticFilesystemMetadataProvider">
            <constructor-arg>
                <value type="java.io.File">classpath:metadata/idp.xml</value>
            </constructor-arg>
            <property name="parserPool" ref="parserPool"/>
        </bean>
    </constructor-arg>
    <constructor-arg>
        <bean class="org.springframework.security.saml.metadata.ExtendedMetadata">
        </bean>
    </constructor-arg>
</bean>
```
