package io.github.areguig.echoproxy.configuration;

import io.github.areguig.echoproxy.controller.ReactiveProxyConfigController;
import io.github.areguig.echoproxy.handler.ProxyHandler;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import io.github.areguig.echoproxy.service.ReactiveProxyService;
import io.github.areguig.echoproxy.service.RequestLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxConfiguration {
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ProxyHandler proxyHandler(ReactiveProxyService proxyService) {
        return new ProxyHandler(proxyService);
    }

    @Bean
    public ReactiveProxyService proxyService(
            ConfigurationStorage configStorage,
            WebClient.Builder webClientBuilder,
            RequestLogService requestLogService) {
        return new ReactiveProxyService(configStorage, webClientBuilder,
                                requestLogService);
    }
    @Bean
    public RouterFunction<ServerResponse> route(ProxyHandler handler) {
        return RouterFunctions.route(RequestPredicates.all(), handler::handleRequest);
    }
    @Bean
    public ReactiveProxyConfigController proxyConfigController(
            ConfigurationStorage configurationStorage) {
        return new ReactiveProxyConfigController(configurationStorage);
    }
}