package io.github.areguig.echoproxy.handler;

import io.github.areguig.echoproxy.service.ReactiveProxyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Component
public class ProxyHandler {
    private final ReactiveProxyService proxyService;

    public ProxyHandler(ReactiveProxyService proxyService) {
        this.proxyService = proxyService;
    }

    public Mono<ServerResponse> handleRequest(ServerRequest serverRequest) {
        return proxyService.handleRequest(serverRequest)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}