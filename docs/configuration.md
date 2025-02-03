---
layout: default
title: Configuration
nav_order: 2
---

# Configuration
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Properties

Available configuration properties in `application.properties` or `application.yml`:

| Property | Description | Default |
|----------|-------------|---------|
| `echo.proxy.enabled` | Enable/disable the proxy | `true` |
| `echo.proxy.management-base-path` | Base path for management endpoints | `/echo-proxy` |
| `echo.proxy.config-file` | Path to JSON configuration file | `echo-proxy-config.json` |

Example:
```yaml
echo:
  proxy:
    enabled: true
    management-base-path: /echo-proxy
    config-file: my-config.json
```

## JSON Configuration

You can configure proxy endpoints using a JSON file:

```json
[
  {
    "urlPattern": "/api/test",
    "mode": "MOCK",
    "method": "GET",
    "responseBody": "{\"message\":\"test\"}",
    "statusCode": 200,
    "responseHeaders": {
      "Content-Type": "application/json",
      "X-Custom-Header": "test-value"
    },
    "active": true
  }
]
```

## Programmatic Configuration

Configure endpoints programmatically using the `ConfigurationStorage` service:

```java
@Autowired
private ConfigurationStorage configStorage;

ProxyConfig config = new ProxyConfig();
config.setUrlPattern("/api/test");
config.setMode(ProxyMode.MOCK);
config.setMethod(HttpMethod.GET);
config.setResponseBody("{\"message\":\"test\"}");
config.setStatusCode(200);
config.setActive(true);

configStorage.addConfig(config);
``` 