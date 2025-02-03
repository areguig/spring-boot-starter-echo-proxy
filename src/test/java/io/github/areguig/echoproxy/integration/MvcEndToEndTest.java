package io.github.areguig.echoproxy.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.github.areguig.echoproxy.configuration.EchoProxyAutoConfiguration;
import io.github.areguig.echoproxy.configuration.EchoProxyProperties;
import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(
    classes = MvcEndToEndTest.TestConfig.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.main.web-application-type=servlet",
        "echo.proxy.enabled=true",
        "spring.main.allow-bean-definition-overriding=true"
    }
)
@AutoConfigureMockMvc
@WireMockTest(httpPort = 8080)
class MvcEndToEndTest {

    @Configuration
    @EnableAutoConfiguration
    @EnableConfigurationProperties(EchoProxyProperties.class)
    @Import(EchoProxyAutoConfiguration.class)
    static class TestConfig {}

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConfigurationStorage configStorage;

    @BeforeEach
    void setUp() {
        configStorage.getAllConfigs().forEach(config -> 
            configStorage.deleteConfig(config.getId()));
    }

    @Test
    void shouldHandleMockEndpoint() throws Exception {
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
        mockMvc.perform(get("/api/test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("X-Custom-Header", "test-value"))
                .andExpect(content().json("{\"message\":\"test\"}"));
    }

    @Test
    void shouldReturn404WhenDisabled() throws Exception {
        mockMvc.perform(get("/api/disabled")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldHandleProxyEndpoint() throws Exception {
        // Given
        String responseBody = "{\"proxied\":true}";
        String path = "/api/proxy-test";
        
        // Configure mock response
        stubFor(
            com.github.tomakehurst.wiremock.client.WireMock.get(path)
                .willReturn(aResponse()
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
        mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("X-Proxy-Header", "proxy-value"))
                .andExpect(header().string("X-Additional-Header", "additional-value"))
                .andExpect(content().json(responseBody));
    }
} 