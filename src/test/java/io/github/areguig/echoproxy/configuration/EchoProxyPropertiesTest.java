package io.github.areguig.echoproxy.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = EchoProxyPropertiesTest.TestConfig.class,
    properties = {
        "echo.proxy.config-file=echo-proxy-config.json"
    }
)
class EchoProxyPropertiesTest {

    @Configuration
    @EnableConfigurationProperties(EchoProxyProperties.class)
    static class TestConfig {}

    @Autowired
    private EchoProxyProperties properties;

    @Test
    void shouldUseDefaultProperties() {
        assertThat(properties.isEnabled()).isTrue();
        assertThat(properties.getManagementBasePath()).isEqualTo("/echo-proxy");
        assertThat(properties.getConfigFile()).isEqualTo("echo-proxy-config.json");
    }
} 