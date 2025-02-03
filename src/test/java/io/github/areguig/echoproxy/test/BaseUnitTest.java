package io.github.areguig.echoproxy.test;

import io.github.areguig.echoproxy.configuration.EchoProxyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
    "echo.proxy.enabled=true",
    "echo.proxy.management-base-path=/echo-proxy",
    "echo.proxy.config-file=echo-proxy-config.json"
})
@Import(BaseUnitTest.TestConfig.class)
public abstract class BaseUnitTest {

    @TestConfiguration
    @EnableConfigurationProperties(EchoProxyProperties.class)
    public static class TestConfig {
        @Bean
        public EchoProxyProperties echoProxyProperties() {
            EchoProxyProperties properties = new EchoProxyProperties();
            properties.setEnabled(true);
            properties.setManagementBasePath("/echo-proxy");
            properties.setConfigFile("echo-proxy-config.json");
            return properties;
        }
    }
} 