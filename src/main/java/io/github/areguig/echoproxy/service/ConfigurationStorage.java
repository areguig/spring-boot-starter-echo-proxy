package io.github.areguig.echoproxy.service;

import io.github.areguig.echoproxy.model.ProxyConfig;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Optional;

public interface ConfigurationStorage {
    List<ProxyConfig> getAllConfigs();
    ProxyConfig addConfig(ProxyConfig config);
    Optional<ProxyConfig> updateConfig(ProxyConfig config);
    boolean deleteConfig(Long id);
    void initialize(List<ProxyConfig> configs);
    Optional<ProxyConfig> findMatchingConfig(String path, HttpMethod method);
} 