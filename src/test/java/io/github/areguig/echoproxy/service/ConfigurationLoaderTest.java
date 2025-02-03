package io.github.areguig.echoproxy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.areguig.echoproxy.configuration.EchoProxyProperties;
import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationLoaderTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EchoProxyProperties properties = new EchoProxyProperties();
    private final ConfigurationLoader loader = new ConfigurationLoader(objectMapper, properties);

    @Test
    void shouldLoadValidConfiguration() {
        // Given
        properties.setConfigFile("test-config.json");
        
        // When
        List<ProxyConfig> configs = loader.loadInitialConfiguration();
        
        // Then
        assertThat(configs)
            .isNotNull()
            .hasSize(1);
        
        ProxyConfig config = configs.getFirst();
        assertThat(config.getUrlPattern()).isEqualTo("/test-endpoint");
        assertThat(config.getMode()).isEqualTo(ProxyMode.MOCK);
    }

    @Test
    void shouldReturnEmptyListWhenConfigFileNotFound() {
        // Given
        properties.setConfigFile("non-existent.json");
        
        // When
        List<ProxyConfig> configs = loader.loadInitialConfiguration();
        
        // Then
        assertThat(configs)
            .isNotNull()
            .isEmpty();
    }
} 