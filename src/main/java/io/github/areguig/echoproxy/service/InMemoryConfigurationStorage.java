package io.github.areguig.echoproxy.service;

import io.github.areguig.echoproxy.model.ProxyConfig;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

@Service
public class InMemoryConfigurationStorage implements ConfigurationStorage {
    private final ConcurrentHashMap<Long, ProxyConfig> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public List<ProxyConfig> getAllConfigs() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public ProxyConfig addConfig(ProxyConfig config) {
        config.setId(idGenerator.incrementAndGet());
        storage.put(config.getId(), config);
        return config;
    }

    @Override
    public Optional<ProxyConfig> updateConfig(ProxyConfig config) {
        if (config.getId() == null || !storage.containsKey(config.getId())) {
            return Optional.empty();
        }
        storage.put(config.getId(), config);
        return Optional.of(config);
    }

    @Override
    public boolean deleteConfig(Long id) {
        return storage.remove(id) != null;
    }

    @Override
    public void initialize(List<ProxyConfig> configs) {
        configs.forEach(this::addConfig);
    }

    @Override
    public Optional<ProxyConfig> findMatchingConfig(String path, HttpMethod method) {
        return storage.values().stream()
            .filter(config -> config.isActive() &&
                            config.getMethod() == method &&
                            Pattern.matches(config.getUrlPattern(), path))
            .findFirst();
    }
} 