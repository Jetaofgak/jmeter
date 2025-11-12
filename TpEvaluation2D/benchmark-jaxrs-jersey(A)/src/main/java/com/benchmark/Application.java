package com.benchmark;

import com.benchmark.resource.CategoryResource;
import com.benchmark.resource.ItemResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {

    public Application() {
        // Enregistrer les resources
        register(CategoryResource.class);
        register(ItemResource.class);

        // Enregistrer Jackson pour JSON
        register(org.glassfish.jersey.jackson.JacksonFeature.class);

        // Enregistrer le provider pour LocalDateTime
        register(ObjectMapperContextResolver.class);
    }

    @Provider
    public static class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

        private final ObjectMapper mapper;

        public ObjectMapperContextResolver() {
            mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }

        @Override
        public ObjectMapper getContext(Class<?> type) {
            return mapper;
        }
    }
}
