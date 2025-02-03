package io.github.areguig.echoproxy.integration;

import io.github.areguig.echoproxy.test.TestApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.main.web-application-type=reactive",
        "spring.main.allow-bean-definition-overriding=true"
    }
)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
} 