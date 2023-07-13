package com.github.marceloleite2604.cryptotrader.configuration;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class GeneralConfiguration {
  public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

  public static final BigDecimal COMPARISON_THRESHOLD = BigDecimal.valueOf(0.1);

  public static final BigDecimal LOGICAL_ZERO = BigDecimal.valueOf(0.0000001);
}
