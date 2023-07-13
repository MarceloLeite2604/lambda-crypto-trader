package com.github.marceloleite2604.cryptotrader.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.marceloleite2604.cryptotrader.dto.serializer.DoubleStdSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

  @Bean(BeanNames.MODULE)
  public Module createModule() {
    SimpleModule module = new SimpleModule();
    module.addSerializer(Double.class, new DoubleStdSerializer());
    return module;
  }

  @Bean(BeanNames.OBJECT_MAPPER)
  public ObjectMapper createObjectMapper(Module module) {
    final var objectMapper = new ObjectMapper();
    objectMapper.registerModule(module);
    return objectMapper;
  }
}
