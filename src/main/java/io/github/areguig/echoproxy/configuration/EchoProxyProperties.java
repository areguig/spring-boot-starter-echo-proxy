package io.github.areguig.echoproxy.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "echo.proxy")
public class EchoProxyProperties {
    
    /**
     * Enable/disable the proxy functionality
     */
    private boolean enabled = true;

    /**
     * Base path for management endpoints
     */
    private String managementBasePath = "/echo-proxy";

    /**
     * Configuration file path
     */
    private String configFile = "echo-proxy-config.json";

    // Getters and setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getManagementBasePath() {
        return managementBasePath;
    }

    public void setManagementBasePath(String managementBasePath) {
        this.managementBasePath = managementBasePath;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
} 