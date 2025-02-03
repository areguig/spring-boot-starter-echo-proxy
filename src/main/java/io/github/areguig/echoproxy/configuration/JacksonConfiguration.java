package io.github.areguig.echoproxy.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        
        // Add serializer/deserializer for HttpMethod
        module.addSerializer(HttpMethod.class, new HttpMethodSerializer());
        module.addDeserializer(HttpMethod.class, new HttpMethodDeserializer());
        
        objectMapper.registerModule(module);
        return objectMapper;
    }
} 