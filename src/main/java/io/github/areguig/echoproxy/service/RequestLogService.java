package io.github.areguig.echoproxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RequestLogService {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogService.class);

    public String logRequest(String path, HttpMethod method, String body) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Request {} - {} {} - Body: {}", 
                   requestId, method, path, body);
        return requestId;
    }

    public void logResponse(String requestId, ResponseEntity<String> response) {
        logger.info("Response {} - Status: {} - Body: {}", 
                   requestId, 
                   response.getStatusCode(), 
                   response.getBody());
    }

    public void logError(String requestId, Throwable error) {
        logger.error("Error {} - Message: {}", 
                    requestId, 
                    error.getMessage(), 
                    error);
    }
} 