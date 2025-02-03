package io.github.areguig.echoproxy.service;

import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryConfigurationStorageTest {

    private InMemoryConfigurationStorage storage;

    @BeforeEach
    void setUp() {
        storage = new InMemoryConfigurationStorage();
    }

    @Test
    void shouldAddAndRetrieveConfig() {
        ProxyConfig config = new ProxyConfig();
        config.setUrlPattern("/test");
        config.setMode(ProxyMode.MOCK);

        ProxyConfig saved = storage.addConfig(config);
        assertThat(saved.getId()).isNotNull();
        assertThat(storage.getAllConfigs()).hasSize(1);
    }

    @Test
    void shouldUpdateExistingConfig() {
        ProxyConfig config = new ProxyConfig();
        config.setUrlPattern("/test");
        config = storage.addConfig(config);

        config.setUrlPattern("/updated");
        var updated = storage.updateConfig(config);

        assertThat(updated).isPresent();
        assertThat(updated.get().getUrlPattern()).isEqualTo("/updated");
    }

    @Test
    void shouldNotUpdateNonExistingConfig() {
        ProxyConfig config = new ProxyConfig();
        config.setId(999L);
        config.setUrlPattern("/test");

        var updated = storage.updateConfig(config);
        assertThat(updated).isEmpty();
    }

    @Test
    void shouldDeleteConfig() {
        ProxyConfig config = new ProxyConfig();
        config = storage.addConfig(config);

        boolean deleted = storage.deleteConfig(config.getId());
        assertThat(deleted).isTrue();
        assertThat(storage.getAllConfigs()).isEmpty();
    }
} 