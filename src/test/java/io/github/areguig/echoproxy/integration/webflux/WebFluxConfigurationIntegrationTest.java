package io.github.areguig.echoproxy.integration.webflux;

import io.github.areguig.echoproxy.controller.ProxyController;
import io.github.areguig.echoproxy.handler.ProxyHandler;
import io.github.areguig.echoproxy.test.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("webflux")
class WebFluxConfigurationIntegrationTest {

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

@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "echo.proxy.enabled=false"
)
@ActiveProfiles("webflux")
class DisabledWebFluxConfigurationIntegrationTest {

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