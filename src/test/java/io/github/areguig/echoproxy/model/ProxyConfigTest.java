package io.github.areguig.echoproxy.model;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProxyConfigTest {

    @Test
    void shouldHandleResponseHeaders() {
        // Given
        ProxyConfig config = new ProxyConfig();
        Map<String, String> headers = Map.of(
            "Content-Type", MediaType.APPLICATION_JSON_VALUE,
            "X-Custom-Header", "test-value"
        );

        // When
        config.setResponseHeaders(headers);

        // Then
        assertThat(config.getResponseHeaders())
            .containsAllEntriesOf(headers)
            .hasSize(2);
    }

    @Test
    void shouldCreateValidConfig() {
        // Given
        ProxyConfig config = new ProxyConfig();
        
        // When
        config.setUrlPattern("/test/**");
        config.setMode(ProxyMode.MOCK);
        config.setMethod(HttpMethod.GET);
        config.setResponseBody("{\"test\":true}");
        config.setStatusCode(200);
        config.setResponseHeaders(Map.of("Content-Type", "application/json"));
        
        // Then
        assertThat(config.getUrlPattern()).isEqualTo("/test/**");
        assertThat(config.getMode()).isEqualTo(ProxyMode.MOCK);
        assertThat(config.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(config.getResponseBody()).isEqualTo("{\"test\":true}");
        assertThat(config.getStatusCode()).isEqualTo(200);
        assertThat(config.getResponseHeaders())
            .containsEntry("Content-Type", "application/json");
        assertThat(config.isActive()).isTrue();
    }
} 