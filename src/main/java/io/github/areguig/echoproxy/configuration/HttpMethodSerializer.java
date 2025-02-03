package io.github.areguig.echoproxy.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.http.HttpMethod;

import java.io.IOException;

public class HttpMethodSerializer extends StdSerializer<HttpMethod> {

    public HttpMethodSerializer() {
        super(HttpMethod.class);
    }

    @Override
    public void serialize(HttpMethod value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.name());
    }
} 