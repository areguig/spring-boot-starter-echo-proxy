# Spring Boot Starter Echo Proxy

A Spring Boot starter that provides a configurable proxy/mock server for development and testing purposes. It allows you to easily mock external services or proxy requests to real endpoints with the ability to modify responses.

## Features

- Mock HTTP endpoints with configurable responses
- Proxy requests to external services
- Dynamic configuration of endpoints via JSON or programmatic API
- Support for custom response headers
- Response modification capabilities
- Easy integration with Spring Boot applications
- Support for both Spring MVC and WebFlux stacks

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.areguig</groupId>
    <artifactId>echo-proxy-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Configuration

### Properties

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

### JSON Configuration

You can configure proxy endpoints using a JSON file. Create a file named `echo-proxy-config.json` (or as specified in `echo.proxy.config-file`):

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
  },
  {
    "urlPattern": "/api/proxy",
    "mode": "PROXY",
    "method": "GET",
    "targetUrl": "http://external-service.com",
    "responseHeaders": {
      "X-Additional-Header": "custom-value"
    },
    "active": true
  }
]
```

### Programmatic Configuration

You can also configure endpoints programmatically:

```java
@Autowired
private ConfigurationStorage configStorage;

// Mock endpoint example
ProxyConfig mockConfig = new ProxyConfig();
mockConfig.setUrlPattern("/api/test");
mockConfig.setMode(ProxyMode.MOCK);
mockConfig.setMethod(HttpMethod.GET);
mockConfig.setResponseBody("{\"message\":\"test\"}");
mockConfig.setStatusCode(200);
mockConfig.setResponseHeaders(Map.of(
    "Content-Type", "application/json",
    "X-Custom-Header", "test-value"
));
mockConfig.setActive(true);
configStorage.addConfig(mockConfig);

// Proxy endpoint example
ProxyConfig proxyConfig = new ProxyConfig();
proxyConfig.setUrlPattern("/api/proxy");
proxyConfig.setMode(ProxyMode.PROXY);
proxyConfig.setMethod(HttpMethod.GET);
proxyConfig.setTargetUrl("http://external-service.com");
proxyConfig.setActive(true);
configStorage.addConfig(proxyConfig);
```

### Management API

The library exposes REST endpoints to manage proxy configurations:

- `GET /echo-proxy/configs` - List all configurations
- `POST /echo-proxy/configs` - Create new configuration
- `PUT /echo-proxy/configs/{id}` - Update existing configuration
- `DELETE /echo-proxy/configs/{id}` - Delete configuration

## Web Stack Support

The library automatically detects and works with both Spring MVC and Spring WebFlux stacks. No additional configuration is needed.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.