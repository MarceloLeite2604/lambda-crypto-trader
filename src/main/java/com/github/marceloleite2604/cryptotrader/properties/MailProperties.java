package com.github.marceloleite2604.cryptotrader.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@ConfigurationProperties(PropertiesPath.MAIL)
@Validated
@Getter
@ConstructorBinding
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MailProperties {

  @NotBlank
  private final String username;

  @NotBlank
  private final String password;

  @NotBlank
  private final String host;

  @NotBlank
  private final String port;

  @NotEmpty
  private final List<String> recipients;
}
