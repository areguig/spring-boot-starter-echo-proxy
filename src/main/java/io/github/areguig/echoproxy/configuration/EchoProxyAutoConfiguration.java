package io.github.areguig.echoproxy.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.areguig.echoproxy.service.ConfigurationLoader;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import io.github.areguig.echoproxy.service.InMemoryConfigurationStorage;
import io.github.areguig.echoproxy.service.RequestLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(EchoProxyProperties.class)
@ConditionalOnProperty(name = "echo.proxy.enabled", matchIfMissing = true)
@Import({JacksonConfiguration.class, WebFluxConfiguration.class, WebMvcConfiguration.class})
public class EchoProxyAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigurationLoader configurationLoader(ObjectMapper objectMapper,
                                                   EchoProxyProperties properties) {
        return new ConfigurationLoader(objectMapper, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConfigurationStorage configurationStorage(ConfigurationLoader loader) {
        InMemoryConfigurationStorage storage = new InMemoryConfigurationStorage();
        storage.initialize(loader.loadInitialConfiguration());
        return storage;
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestLogService requestLogService() {
        return new RequestLogService();
    }

} 