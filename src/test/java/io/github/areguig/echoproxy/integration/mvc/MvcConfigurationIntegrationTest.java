package io.github.areguig.echoproxy.integration.mvc;

import io.github.areguig.echoproxy.controller.ProxyController;
import io.github.areguig.echoproxy.test.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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