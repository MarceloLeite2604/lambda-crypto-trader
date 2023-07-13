package com.github.marceloleite2604.cryptotrader.util;

import com.github.marceloleite2604.cryptotrader.configuration.GeneralConfiguration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ComparisonUtil {

  public int compareRatioUsingMargin(BigDecimal first, BigDecimal second) {
    return compareRatioUsingMargin(first, second, GeneralConfiguration.COMPARISON_THRESHOLD);
  }

  public int compareRatioUsingMargin(BigDecimal first, BigDecimal second, BigDecimal margin) {

    var scaledFirst = first;
    var scaledSecond = second;

    if (scaledFirst.scale() < margin.scale()) {
      scaledFirst = scaledFirst.setScale(margin.scale(), GeneralConfiguration.DEFAULT_ROUNDING_MODE);
    }

    if (scaledSecond.scale() < margin.scale()) {
      scaledSecond = scaledSecond.setScale(margin.scale(), GeneralConfiguration.DEFAULT_ROUNDING_MODE);
    }

    final var ratio = scaledSecond
      .compareTo(BigDecimal.ZERO) == 0 ?
      BigDecimal.ZERO : scaledFirst.divide(scaledSecond, GeneralConfiguration.DEFAULT_ROUNDING_MODE);

    if (ratio.compareTo(margin) > 0) {
      return 1;
    } else if (ratio.compareTo(margin.negate()) < 0) {
      return -1;
    }

    return 0;
  }
}
