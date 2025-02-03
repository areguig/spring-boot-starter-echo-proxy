package io.github.areguig.echoproxy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.configuration.EchoProxyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigurationLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);
    private final ObjectMapper objectMapper;
    private final EchoProxyProperties properties;

    public ConfigurationLoader(ObjectMapper objectMapper, EchoProxyProperties properties) {
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    public List<ProxyConfig> loadInitialConfiguration() {
        try {
            ClassPathResource resource = new ClassPathResource(properties.getConfigFile());
            try (InputStream is = resource.getInputStream()) {
                return objectMapper.readValue(is, new TypeReference<>() {
                });
            }
        } catch (IOException e) {
            logger.warn("Could not load initial configuration from {}: {}", 
                properties.getConfigFile(), e.getMessage());
            return new ArrayList<>();
        }
    }
} 