package io.github.areguig.echoproxy.service;

import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MvcProxyService {
    private final ConfigurationStorage configStorage;
    private final RestTemplate restTemplate;
    private final RequestLogService logService;

    public MvcProxyService(ConfigurationStorage configStorage,
                          RestTemplate restTemplate,
                          RequestLogService logService) {
        this.configStorage = configStorage;
        this.restTemplate = restTemplate;
        this.logService = logService;
    }

    public ResponseEntity<String> handleRequest(String path,
                                              HttpMethod method,
                                              String body,
                                              HttpHeaders headers) {
        String requestId = logService.logRequest(path, method, body);
        Optional<ProxyConfig> config = configStorage.findMatchingConfig(path, method);
        if (config.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            ResponseEntity<String> response = config.get().getMode() == ProxyMode.MOCK ?
                    getMockResponse(config.get()) :
                    forwardRequest(config.get(), path, method, body, headers);
            
            logService.logResponse(requestId, response);
            return response;
        } catch (Exception error) {
            logService.logError(requestId, error);
            throw error;
        }
    }

    public ResponseEntity<String> getMockResponse(ProxyConfig config) {
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity
            .status(config.getStatusCode() != null ? config.getStatusCode() : 200);

        if (config.getResponseHeaders() != null) {
            config.getResponseHeaders().forEach(responseBuilder::header);
        }

        return responseBuilder.body(config.getResponseBody());
    }

    private ResponseEntity<String> forwardRequest(ProxyConfig config,
                                                String path,
                                                HttpMethod method,
                                                String body,
                                                HttpHeaders requestHeaders) {
        HttpEntity<String> requestEntity = new HttpEntity<>(body, requestHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                config.getTargetUrl() + path,
                method,
                requestEntity,
                String.class
        );

        if (config.getResponseHeaders() != null) {
            ResponseEntity.BodyBuilder responseBuilder = ResponseEntity
                .status(response.getStatusCode());

            // Add original headers
            response.getHeaders().forEach((key, value) -> 
                responseBuilder.header(key, value.toArray(new String[0])));

            // Add/override with config headers
            config.getResponseHeaders().forEach(responseBuilder::header);

            return responseBuilder.body(response.getBody());
        }

        return response;
    }
} 