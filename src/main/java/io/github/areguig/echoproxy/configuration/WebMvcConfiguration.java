package io.github.areguig.echoproxy.configuration;

import io.github.areguig.echoproxy.controller.ProxyConfigController;
import io.github.areguig.echoproxy.controller.ProxyController;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import io.github.areguig.echoproxy.service.MvcProxyService;
import io.github.areguig.echoproxy.service.RequestLogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebMvcConfiguration {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public MvcProxyService mvcProxyService(ConfigurationStorage configStorage,
                                           RestTemplate restTemplate,
                                           RequestLogService logService) {
        return new MvcProxyService(configStorage, restTemplate, logService);
    }
    @Bean
    public ProxyController proxyController(MvcProxyService proxyService) {
        return new ProxyController(proxyService);
    }

    @Bean
    public ProxyConfigController proxyConfigController(ConfigurationStorage configurationStorage) {
        return new ProxyConfigController(configurationStorage);
    }
}