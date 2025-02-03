package io.github.areguig.echoproxy.service;

import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProxyServiceTest {

    @Mock
    private ConfigurationStorage configStorage;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    private RequestLogService logService;

    private ReactiveProxyService proxyService;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.build()).thenReturn(webClient);
        proxyService = new ReactiveProxyService(configStorage, webClientBuilder, logService);
    }

    @Test
    void shouldReturnMockResponse() {
        // Given
        ProxyConfig config = new ProxyConfig();
        config.setMode(ProxyMode.MOCK);
        config.setUrlPattern("/test");
        config.setMethod(HttpMethod.GET);
        config.setResponseBody("{\"message\":\"mocked\"}");
        config.setStatusCode(200);
        config.setResponseHeaders(Map.of(
            "Content-Type", MediaType.APPLICATION_JSON_VALUE,
            "X-Test", "test-value"
        ));

        ServerRequest request = mock(ServerRequest.class);
        when(request.path()).thenReturn("/test");
        when(request.method()).thenReturn(HttpMethod.GET);
        when(request.bodyToMono(String.class)).thenReturn(Mono.just(""));
        when(configStorage.findMatchingConfig("/test", HttpMethod.GET))
            .thenReturn(Optional.of(config));
        when(logService.logRequest(anyString(), any(), anyString())).thenReturn("test-id");

        // When/Then
        StepVerifier.create(proxyService.handleRequest(request))
            .expectNextMatches(response -> 
                response.statusCode().value() == 200 &&
                        Objects.equals(response.headers().getContentType(), MediaType.APPLICATION_JSON) &&
                        Objects.equals(response.headers().getFirst("X-Test"), "test-value"))
            .verifyComplete();
    }

    @Test
    void shouldReturn404WhenNoConfigFound() {
        // Given
        ServerRequest request = mock(ServerRequest.class);
        when(request.path()).thenReturn("/not-found");
        when(request.method()).thenReturn(HttpMethod.GET);
        when(request.bodyToMono(String.class)).thenReturn(Mono.just(""));
        when(configStorage.findMatchingConfig("/not-found", HttpMethod.GET))
            .thenReturn(Optional.empty());

        // When/Then
        StepVerifier.create(proxyService.handleRequest(request))
            .expectNextMatches(response -> response.statusCode().value() == 404)
            .verifyComplete();
    }
} 