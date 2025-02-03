package io.github.areguig.echoproxy.controller;

import io.github.areguig.echoproxy.ReactiveTestConfiguration;
import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@WebFluxTest(ReactiveProxyConfigController.class)
@Import(ReactiveTestConfiguration.class)
public class ReactiveProxyConfigControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private ConfigurationStorage configurationStorage;

    @Test
    public void getAllConfigs() {
        ProxyConfig config = new ProxyConfig();
        config.setId(1L);
        config.setUrlPattern("/test");
        config.setMode(ProxyMode.MOCK);
        
        when(configurationStorage.getAllConfigs())
            .thenReturn(List.of(config));

        webClient.get().uri("/echo-proxy/configs")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProxyConfig.class)
                .hasSize(1)
                .contains(config);
    }

    // ... similar tests for other endpoints ...
} 