package com.github.marceloleite2604.cryptotrader.properties;

import com.github.marceloleite2604.cryptotrader.model.candles.CandlePrecision;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@ConfigurationProperties(PropertiesPath.MONITORING)
@Validated
@Getter
@ConstructorBinding
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MonitoringProperties {

  @NotEmpty
  private final List<String> actives;

  @NotNull
  private final CandlePrecision precision;

  @Positive
  private final int quantity;

  @Positive
  private final BigDecimal profitThreshold;
}
