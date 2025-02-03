package io.github.areguig.echoproxy.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.areguig.echoproxy.configuration.EchoProxyAutoConfiguration;
import io.github.areguig.echoproxy.configuration.EchoProxyProperties;
import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
    classes = ReactiveEndToEndTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.main.web-application-type=reactive",
        "echo.proxy.enabled=true",
        "spring.main.allow-bean-definition-overriding=true"
    }
)
@AutoConfigureWebTestClient
@WireMockTest(httpPort = 8080)
class ReactiveEndToEndTest {

    @Configuration
    @EnableAutoConfiguration
    @EnableConfigurationProperties(EchoProxyProperties.class)
    @Import(EchoProxyAutoConfiguration.class)
    static class TestConfig {}

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ConfigurationStorage configStorage;

    @BeforeEach
    void setUp() {
        configStorage.getAllConfigs().forEach(config -> 
            configStorage.deleteConfig(config.getId()));
    }

    @Test
    void shouldHandleMockEndpoint() {
        // Given
        ProxyConfig config = new ProxyConfig();
        config.setUrlPattern("/api/test");
        config.setMode(ProxyMode.MOCK);
        config.setMethod(HttpMethod.GET);
        config.setResponseBody("{\"message\":\"test\"}");
        config.setStatusCode(200);
        config.setResponseHeaders(Map.of(
            "Content-Type", MediaType.APPLICATION_JSON_VALUE,
            "X-Custom-Header", "test-value"
        ));
        config.setActive(true);

        configStorage.addConfig(config);

        // When/Then
        webClient.get().uri("/api/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectHeader().valueEquals("X-Custom-Header", "test-value")
                .expectBody()
                .json("{\"message\":\"test\"}");
    }

    @Test
    void shouldReturn404WhenDisabled() {
        webClient.get().uri("/api/disabled")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldHandleProxyEndpoint() {
        // Given
        String responseBody = "{\"proxied\":true}";
                                            String path = "/api/proxy-test";
        
        // Configure mock response
        com.github.tomakehurst.wiremock.client.WireMock.stubFor(
            com.github.tomakehurst.wiremock.client.WireMock.get(path)
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withHeader("X-Proxy-Header", "proxy-value")
                    .withBody(responseBody)));

        // Configure proxy
        ProxyConfig config = new ProxyConfig();
        config.setUrlPattern(path);
        config.setMode(ProxyMode.PROXY);
        config.setMethod(HttpMethod.GET);
        config.setTargetUrl("http://localhost:8080");
        config.setResponseHeaders(Map.of(
            "X-Additional-Header", "additional-value"
        ));
        config.setActive(true);
        
        configStorage.addConfig(config);

        // When/Then
        webClient.get().uri(path)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().valueEquals("X-Proxy-Header", "proxy-value")
                .expectHeader().valueEquals("X-Additional-Header", "additional-value")
                .expectBody()
                .json(responseBody);
    }
} 