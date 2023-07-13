package com.github.marceloleite2604.cryptotrader.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@ConfigurationProperties(PropertiesPath.MERCADO_BITCOIN)
@Validated
@ConstructorBinding
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MercadoBitcoinProperties {

  @NotBlank
  private final String baseUri;

  @NotBlank
  private final String clientId;

  @NotBlank
  private final String clientSecret;
}
