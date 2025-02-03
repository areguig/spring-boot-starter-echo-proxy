package io.github.areguig.echoproxy.integration.mvc;

import io.github.areguig.echoproxy.configuration.EchoProxyAutoConfiguration;
import io.github.areguig.echoproxy.configuration.EchoProxyProperties;
import io.github.areguig.echoproxy.controller.ProxyController;
import io.github.areguig.echoproxy.test.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class,
        properties = "echo.proxy.enabled=true")
@ActiveProfiles("mvc")
class MvcConfigurationIntegrationTest {

    @Autowired(required = false)
    private ProxyController proxyController;

    @Test
    void shouldLoadMvcConfiguration() {
        assertThat(proxyController).isNotNull();
    }
}

@SpringBootTest(classes = TestApplication.class,
        properties = "echo.proxy.enabled=false")
@ActiveProfiles("mvc")
class DisabledMvcConfigurationIntegrationTest {

    @Autowired(required = false)
    private ProxyController proxyController;

    @Test
    void shouldNotLoadMvcConfigurationWhenDisabled() {
        assertThat(proxyController).isNull();
    }
} 