package io.github.areguig.echoproxy.integration.webflux;

import io.github.areguig.echoproxy.controller.ProxyController;
import io.github.areguig.echoproxy.handler.ProxyHandler;
import io.github.areguig.echoproxy.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("webflux")
class WebFluxConfigurationIntegrationTest extends AbstractIntegrationTest {

    @Autowired(required = false)
    private ProxyHandler proxyHandler;

    @Autowired(required = false)
    private ProxyController proxyController;

    @Test
    void shouldLoadWebFluxConfiguration() {
        assertThat(proxyHandler).isNotNull();
        assertThat(proxyController).isNull();
    }
}

@ActiveProfiles("webflux")
@TestPropertySource(properties = "echo.proxy.enabled=false")
class DisabledWebFluxConfigurationIntegrationTest extends AbstractIntegrationTest {

    @Autowired(required = false)
    private ProxyHandler proxyHandler;

    @Autowired(required = false)
    private ProxyController proxyController;

    @Test
    void shouldNotLoadWebFluxConfigurationWhenDisabled() {
        assertThat(proxyHandler).isNull();
        assertThat(proxyController).isNull();
    }
} 