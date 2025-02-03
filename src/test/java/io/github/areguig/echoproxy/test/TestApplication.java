package io.github.areguig.echoproxy.test;

import io.github.areguig.echoproxy.configuration.EchoProxyAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(
    scanBasePackages = "io.github.areguig.echoproxy.test",
    exclude = {
        SecurityAutoConfiguration.class,
        ReactiveSecurityAutoConfiguration.class
    }
)
@Import(EchoProxyAutoConfiguration.class)
public class TestApplication {
    // Empty test application for integration tests
} 