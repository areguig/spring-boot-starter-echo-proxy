---
layout: default
title: Home
nav_order: 1
description: "Echo Proxy is a Spring Boot starter that provides a configurable proxy/mock server"
permalink: /
---

# Echo Proxy
{: .fs-9 }

A Spring Boot starter that provides a configurable proxy/mock server for easily mocking external services or proxying requests.
{: .fs-6 .fw-300 }

[Get started now](#getting-started){: .btn .btn-primary .fs-5 .mb-4 .mb-md-0 .mr-2 }
[View it on GitHub](https://github.com/areguig/spring-boot-starter-echo-proxy){: .btn .fs-5 .mb-4 .mb-md-0 }

---

## Getting Started

### Installation

#### Maven

Add the GitHub Packages repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub areguig Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/areguig/spring-boot-starter-echo-proxy</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.github.areguig</groupId>
        <artifactId>spring-boot-starter-echo-proxy</artifactId>
        <version>0.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

#### Gradle

Add the GitHub Packages repository and dependency to your `build.gradle`:

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/areguig/spring-boot-starter-echo-proxy")
    }
}

dependencies {
    implementation 'io.github.areguig:spring-boot-starter-echo-proxy:0.1.0-SNAPSHOT'
}
```

### Quick Start

1. Enable the proxy in your `application.yml`:

```yaml
echo:
  proxy:
    enabled: true
```

2. Create a mock endpoint:

```java
@Autowired
private ConfigurationStorage configStorage;

ProxyConfig mockConfig = new ProxyConfig();
mockConfig.setUrlPattern("/api/test");
mockConfig.setMode(ProxyMode.MOCK);
mockConfig.setMethod(HttpMethod.GET);
mockConfig.setResponseBody("{\"message\":\"test\"}");
mockConfig.setStatusCode(200);
mockConfig.setActive(true);
configStorage.addConfig(mockConfig);
```

## About the project

Echo Proxy is &copy; 2024 by [Akli REGUIG](https://github.com/areguig).

### License

Echo Proxy is distributed under the [MIT License](https://github.com/areguig/spring-boot-starter-echo-proxy/blob/main/LICENSE). 