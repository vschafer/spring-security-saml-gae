# Spring Security SAML GAE

Project enables deployment of Spring SAML applications in Google Application Engine.

## Introduction

Google Application Engine doesn't by default support starting of new threads and direct usage of sockets. The following classes introduce usage of GAE specific APIs for operations involving these components.

### Metadata loading

Provider enables population of SAML meatadata from filesystem. The implementation removes all automated reloading which is not supported by GAE due to limitations on starting of new threads.

**Implementation:** `org.springframework.security.saml.metadata.provider.StaticFilesystemMetadataProvider`

### Artifact resolution

Enables loading of SAML responses using HTTP-Artifact binding using classes available in GAE API.

**Implementation:** `org.springframework.security.saml.websso.google.ArtifactResolutionProfileGAE`

## Usage

### Maven dependency

Include the compiled library (`mvn install`) as a dependency in your Spring SAML project, e.g.:
```
<dependency>
    <groupId>org.springframework.security.extensions</groupId>
    <artifactId>spring-security-saml2-gae</artifactId>
    <version>1.0.0.RELEASE-SNAPSHOT</version>
</dependency>
```

### Spring SAML configuration

In order to install artifact resolution using GAE specific APIs, replace bean `org.springframework.security.saml.websso.ArtifactResolutionProfileImpl` with `org.springframework.security.saml.websso.google.ArtifactResolutionProfileGAE` in your Spring SAML configuration XML. The configuration of the whole artifactBinding will be:

```
<bean id="artifactBinding" class="org.springframework.security.saml.processor.HTTPArtifactBinding">
    <constructor-arg ref="parserPool"/>
    <constructor-arg ref="velocityEngine"/>
    <constructor-arg>
        <bean class="org.springframework.security.saml.websso.google.ArtifactResolutionProfileGAE">
            <property name="processor">
                <bean class="org.springframework.security.saml.processor.SAMLProcessorImpl">
                    <constructor-arg ref="soapBinding"/>
                </bean>
            </property>
        </bean>
    </constructor-arg>
</bean>
```

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

All the other existing providers should be removed from the metadata bean, as they use implementation classes incompatible with GAE.

Also, you need to set property **refreshCheckInterval** on bean **metadata** to value 0:

```
<bean id="metadata" class="org.springframework.security.saml.metadata.CachingMetadataManager">
    <constructor-arg>
        <list>
            ... metadata ...
        </list>
    </constructor-arg>
    <property name="refreshCheckInterval" value="0"/>
</bean>
```

### GAE application descriptor

Spring SAML relies on usage of HTTP sessions. Make sure to enable their usage in `appengine-web.xml` using element `<sessions-enabled>true</sessions-enabled>`.
