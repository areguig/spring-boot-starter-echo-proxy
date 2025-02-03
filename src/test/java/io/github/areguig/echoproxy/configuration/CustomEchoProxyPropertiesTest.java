package io.github.areguig.echoproxy.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = CustomEchoProxyPropertiesTest.TestConfig.class,
    properties = {
        "echo.proxy.enabled=false",
        "echo.proxy.management-base-path=/custom-path",
        "echo.proxy.config-file=custom-config.json"
    }
)
class CustomEchoProxyPropertiesTest {

    @Configuration
    @EnableConfigurationProperties(EchoProxyProperties.class)
    static class TestConfig {
    }

    @Autowired
    private EchoProxyProperties properties;

    @Test
    void shouldLoadCustomProperties() {
        assertThat(properties.isEnabled()).isFalse();
        assertThat(properties.getManagementBasePath()).isEqualTo("/custom-path");
        assertThat(properties.getConfigFile()).isEqualTo("custom-config.json");
    }
} 