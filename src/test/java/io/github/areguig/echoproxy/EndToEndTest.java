package io.github.areguig.echoproxy;

import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import io.github.areguig.echoproxy.test.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.main.web-application-type=reactive",
                "spring.main.allow-bean-definition-overriding=true"
        })
@AutoConfigureWebTestClient
@WireMockTest
class EndToEndTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ConfigurationStorage configStorage;

    @Test
    void shouldHandleMockEndpoint() {
        ProxyConfig config = new ProxyConfig();
        config.setUrlPattern("/api/test");
        config.setMode(ProxyMode.MOCK);
        config.setMethod(HttpMethod.GET);
        config.setResponseBody("{\"message\":\"test\"}");
        config.setStatusCode(200);
        config.setActive(true);

        configStorage.addConfig(config);

        webClient.get().uri("/api/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("test");
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
        // Setup WireMock
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        
        // Configure mock response
        wireMockServer.stubFor(get("/api/proxy-test")
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"proxied\":true}")));

        // Configure proxy
        ProxyConfig config = new ProxyConfig();
        config.setUrlPattern("/api/proxy-test");
        config.setMode(ProxyMode.PROXY);
        config.setMethod(HttpMethod.GET);
        config.setTargetUrl("http://localhost:" + wireMockServer.port());
        config.setActive(true);
        
        configStorage.addConfig(config);

        // Test proxy request
        webClient.get().uri("/api/proxy-test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.proxied").isEqualTo(true);

        wireMockServer.stop();
    }
} 