package io.github.areguig.echoproxy.controller;

import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/echo-proxy/configs")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ProxyConfigController {

    private final ConfigurationStorage configurationStorage;

    public ProxyConfigController(ConfigurationStorage configurationStorage) {
        this.configurationStorage = configurationStorage;
    }

    @GetMapping
    public List<ProxyConfig> getAllConfigs() {
        return configurationStorage.getAllConfigs();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProxyConfig createConfig(@RequestBody ProxyConfig config) {
        return configurationStorage.addConfig(config);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProxyConfig> updateConfig(@PathVariable Long id, 
                                                  @RequestBody ProxyConfig config) {
        config.setId(id);
        return configurationStorage.updateConfig(config)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        return configurationStorage.deleteConfig(id) 
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
} 