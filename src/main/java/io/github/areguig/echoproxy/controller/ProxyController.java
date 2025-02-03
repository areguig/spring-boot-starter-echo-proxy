package io.github.areguig.echoproxy.controller;

import io.github.areguig.echoproxy.service.MvcProxyService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ProxyController {
    private final MvcProxyService proxyService;

    public ProxyController(final MvcProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @RequestMapping("/**")
    public ResponseEntity<? extends Object> handleRequest(final HttpServletRequest request) throws IOException {
        var body = request.getReader().lines().collect(Collectors.joining());
        var serverHttpRequest = new ServletServerHttpRequest(request);

        return this.proxyService.handleRequest(
            request.getRequestURI(),
            HttpMethod.valueOf(request.getMethod()),
            body,
                serverHttpRequest.getHeaders()
        );
    }
} 