package io.github.areguig.echoproxy.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.http.HttpMethod;
import io.github.areguig.echoproxy.configuration.HttpMethodSerializer;
import io.github.areguig.echoproxy.configuration.HttpMethodDeserializer;
import java.util.Map;

@Data
public class ProxyConfig {
    private Long id;
    
    @NotBlank(message = "URL pattern is required")
    private String urlPattern;
    
    @NotNull(message = "Proxy mode is required")
    private ProxyMode mode;
    
    @NotNull
    @JsonSerialize(using = HttpMethodSerializer.class)
    @JsonDeserialize(using = HttpMethodDeserializer.class)
    private HttpMethod method;
    
    private String responseBody;
    
    private Integer statusCode;
    
    private String targetUrl;
    
    private boolean active = true;

    private Map<String, String> responseHeaders;
} 