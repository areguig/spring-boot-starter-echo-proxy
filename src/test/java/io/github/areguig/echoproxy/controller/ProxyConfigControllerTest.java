package io.github.areguig.echoproxy.controller;

import io.github.areguig.echoproxy.test.BaseUnitTest;
import io.github.areguig.echoproxy.model.ProxyConfig;
import io.github.areguig.echoproxy.model.ProxyMode;
import io.github.areguig.echoproxy.service.ConfigurationStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProxyConfigController.class)
class ProxyConfigControllerTest extends BaseUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfigurationStorage configurationStorage;

    @Test
    public void getAllConfigs() throws Exception {
        ProxyConfig config = new ProxyConfig();
        config.setId(1L);
        config.setUrlPattern("/test");
        config.setMode(ProxyMode.MOCK);
        
        when(configurationStorage.getAllConfigs())
            .thenReturn(Arrays.asList(config));

        mockMvc.perform(get("/echo-proxy/configs"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].urlPattern").value("/test"));
    }

    @Test
    public void createConfig() throws Exception {
        ProxyConfig config = new ProxyConfig();
        config.setId(1L);
        when(configurationStorage.addConfig(any())).thenReturn(config);

        mockMvc.perform(post("/echo-proxy/configs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"urlPattern\": \"/test\", \"mode\": \"MOCK\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void updateConfig() throws Exception {
        ProxyConfig config = new ProxyConfig();
        config.setId(1L);
        when(configurationStorage.updateConfig(any())).thenReturn(Optional.of(config));

        mockMvc.perform(put("/echo-proxy/configs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"urlPattern\": \"/test\", \"mode\": \"MOCK\"}"))
               .andExpect(status().isOk());
    }

    @Test
    public void deleteConfig() throws Exception {
        when(configurationStorage.deleteConfig(1L)).thenReturn(true);

        mockMvc.perform(delete("/echo-proxy/configs/1"))
               .andExpect(status().isNoContent());
    }
} 