package com.github.marceloleite2604.cryptotrader.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.marceloleite2604.cryptotrader.properties.MercadoBitcoinProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Configuration
public class WebClientConfiguration {

  @Bean(BeanNames.MB_UNAUTHENTICATED_WEB_CLIENT)
  public WebClient createMbUnauthenticatedWebClient(MercadoBitcoinProperties mercadoBitcoinProperties) {
    final var authorizeUri = URI.create(mercadoBitcoinProperties.getBaseUri())
      .resolve("authorize");

    return WebClient.builder()
      .baseUrl(authorizeUri.toString())
      .build();
  }

  @Bean(BeanNames.EXCHANGE_STRATEGIES)
  public ExchangeStrategies createExchangeStrategies(ObjectMapper objectMapper) {
    return ExchangeStrategies
      .builder()
      .codecs(clientDefaultCodecsConfigurer -> {
        clientDefaultCodecsConfigurer.defaultCodecs()
          .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
        clientDefaultCodecsConfigurer.defaultCodecs()
          .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
        clientDefaultCodecsConfigurer.defaultCodecs()
          .maxInMemorySize(1024 * 1024);
      })
      .build();
  }

  @Bean(BeanNames.MB_AUTHENTICATED_WEB_CLIENT)
  public WebClient createMbAuthenticatedWebClient(
    MercadoBitcoinProperties mercadoBitcoinProperties,
    TokenRequestExchangeFilter tokenRequestExchangeFilter,
    ExchangeStrategies exchangeStrategies) {

    return WebClient.builder()
      .baseUrl(mercadoBitcoinProperties.getBaseUri())
      .filter(tokenRequestExchangeFilter)
      .exchangeStrategies(exchangeStrategies)
      .build();
  }
}
