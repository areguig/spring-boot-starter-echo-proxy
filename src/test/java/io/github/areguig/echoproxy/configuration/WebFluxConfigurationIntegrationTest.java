package io.github.areguig.echoproxy.configuration;

import io.github.areguig.echoproxy.handler.ProxyHandler;
import io.github.areguig.echoproxy.test.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("webflux")
class WebFluxConfigurationIntegrationTest {

    @Autowired(required = false)
    private ProxyHandler proxyHandler;

    @Test
    void shouldLoadWebFluxConfiguration() {
        assertThat(proxyHandler).isNotNull();
    }
}

@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("webflux")
@TestPropertySource(properties = "echo.proxy.enabled=false")
class DisabledWebFluxConfigurationIntegrationTest {

    @Autowired(required = false)
    private ProxyHandler proxyHandler;

    @Test
    void shouldNotLoadWebFluxConfigurationWhenDisabled() {
        assertThat(proxyHandler).isNull();
    }
} 