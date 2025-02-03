package io.github.areguig.echoproxy.service;

import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveProxyService {
    private final ConfigurationStorage configStorage;
    private final WebClient webClient;
    private final RequestLogService logService;

    public ReactiveProxyService(ConfigurationStorage configStorage,
                                WebClient.Builder webClientBuilder,
                                RequestLogService logService) {
        this.configStorage = configStorage;
        this.webClient = webClientBuilder.build();
        this.logService = logService;
    }

    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        String path = request.path();
        HttpMethod method = HttpMethod.valueOf(request.methodName());
        
        return request.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body -> {
                    String requestId = logService.logRequest(path, method, body);
                    Optional<ProxyConfig> config = configStorage.findMatchingConfig(path, method);

                    if (config.isEmpty()) {
                        return ServerResponse.notFound().build();
                    }

                    try {
                        Mono<ServerResponse> response = config.get().getMode() == ProxyMode.MOCK ?
                                getMockResponse(config.get()) :
                                forwardRequest(config.get(), request);

                        return response.doOnNext(r -> logResponse(requestId, r));
                    } catch (Exception error) {
                        logService.logError(requestId, error);
                        return Mono.error(error);
                    }
                });
    }

    private Mono<ServerResponse> getMockResponse(ProxyConfig config) {
        ServerResponse.BodyBuilder responseBuilder = ServerResponse
            .status(config.getStatusCode() != null ? config.getStatusCode() : 200);

        if (config.getResponseHeaders() != null) {
            config.getResponseHeaders().forEach(responseBuilder::header);
        }

        return responseBuilder.bodyValue(config.getResponseBody());
    }

    private Mono<ServerResponse> forwardRequest(ProxyConfig config, ServerRequest request) {
        return request.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body -> webClient.method(HttpMethod.valueOf(request.methodName()))
                        .uri(config.getTargetUrl() + request.path())
                        .headers(headers -> request.headers()
                            .asHttpHeaders()
                            .forEach((name, values) -> headers.addAll(name, values)))
                        .bodyValue(body)
                        .exchangeToMono(response -> {
                            ServerResponse.BodyBuilder responseBuilder = ServerResponse
                                .status(response.statusCode());

                            // Add original headers
                            response.headers().asHttpHeaders()
                                .forEach((key, value) -> responseBuilder.header(key, 
                                    value.toArray(new String[0])));

                            // Add/override with config headers
                            if (config.getResponseHeaders() != null) {
                                config.getResponseHeaders()
                                    .forEach(responseBuilder::header);
                            }

                            return response.bodyToMono(String.class)
                                .flatMap(responseBuilder::bodyValue);
                        }));
    }

    private void logResponse(String requestId, ServerResponse response) {
        logService.logResponse(requestId, ResponseEntity.status(response.statusCode())
            .body(response.toString()));
    }
}