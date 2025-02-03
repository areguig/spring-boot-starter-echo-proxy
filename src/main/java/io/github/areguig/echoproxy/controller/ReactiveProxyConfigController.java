package io.github.areguig.echoproxy.controller;

import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/echo-proxy/configs")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveProxyConfigController {

    private final ConfigurationStorage configurationStorage;

    public ReactiveProxyConfigController(ConfigurationStorage configurationStorage) {
        this.configurationStorage = configurationStorage;
    }

    @GetMapping
    public Flux<ProxyConfig> getAllConfigs() {
        return Flux.fromIterable(configurationStorage.getAllConfigs());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProxyConfig> createConfig(@RequestBody ProxyConfig config) {
        return Mono.just(configurationStorage.addConfig(config));
    }

    @PutMapping("/{id}")
    public Mono<ProxyConfig> updateConfig(@PathVariable Long id, @RequestBody ProxyConfig config) {
        config.setId(id);
        return Mono.justOrEmpty(configurationStorage.updateConfig(config));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteConfig(@PathVariable Long id) {
        return Mono.just(configurationStorage.deleteConfig(id))
                .then();
    }
} 