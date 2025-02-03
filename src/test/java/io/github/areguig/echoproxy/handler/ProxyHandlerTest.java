package io.github.areguig.echoproxy.handler;
import io.github.areguig.echoproxy.service.ReactiveProxyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProxyHandlerTest {

    @Mock
    private ReactiveProxyService proxyService;

    @InjectMocks
    private ProxyHandler proxyHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        // Create proper router function configuration
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route(
                RequestPredicates.all(),
                proxyHandler::handleRequest
        );

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .configureClient()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Test
    void shouldHandleRequestBody() {
        var mockResponse = ServerResponse.ok().build();
        when(proxyService.handleRequest(any())).thenReturn(mockResponse);

        webTestClient.post()
                .uri("/body-test")
                .bodyValue("request payload")
                .exchange()
                .expectStatus().isOk();

        verify(proxyService).handleRequest(any());
    }

    @Test
    void shouldReturnOkResponse() {
        // Mock service response
        var mockResponse = ServerResponse.ok()
                .header("Test-Header", "value")
                .bodyValue("test response");

        when(proxyService.handleRequest(any(ServerRequest.class)))
                .thenReturn((mockResponse));

        webTestClient.get()
                .uri("/test-path")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Test-Header", "value")
                .expectBody(String.class).isEqualTo("test response");

        verify(proxyService).handleRequest(any(ServerRequest.class));
    }

    @Test
    void shouldReturnNotFoundWhenServiceReturnsEmpty() {
        when(proxyService.handleRequest(any(ServerRequest.class)))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/non-existent")
                .exchange()
                .expectStatus().isNotFound();

        verify(proxyService).handleRequest(any(ServerRequest.class));
    }

    @Test
    void shouldLogResponseDetails() {
        var mockResponse = ServerResponse.status(HttpStatus.I_AM_A_TEAPOT)
                .header("X-Custom", "123")
                .bodyValue("logging test");

        when(proxyService.handleRequest(any(ServerRequest.class)))
                .thenReturn((mockResponse));

        webTestClient.get()
                .uri("/log-test")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.I_AM_A_TEAPOT);

        verify(proxyService).handleRequest(any(ServerRequest.class));
    }
}