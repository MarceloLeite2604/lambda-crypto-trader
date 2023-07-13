package com.github.marceloleite2604.cryptotrader.util;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Function;

@Component
public class StatisticsUtil {

  private static final StandardDeviation STANDARD_DEVIATION = new StandardDeviation();

  public <T> BigDecimal calculateStandardDeviation(Collection<T> collection, Function<T, BigDecimal> getter) {
    final var averages = collection.stream()
      .map(getter)
      .mapToDouble(BigDecimal::doubleValue)
      .toArray();

    return BigDecimal.valueOf(STANDARD_DEVIATION.evaluate(averages));
  }

  public BigDecimal calculateStandardDeviation(Collection<BigDecimal> collection) {
    final var averages = collection.stream()
      .mapToDouble(BigDecimal::doubleValue)
      .toArray();

    return BigDecimal.valueOf(STANDARD_DEVIATION.evaluate(averages));
  }

  public <T> BigDecimal calculateAverage(Collection<T> collection, Function<T, BigDecimal> getter) {
    return collection.stream()
      .map(getter)
      .mapToDouble(BigDecimal::doubleValue)
      .average()
      .stream()
      .mapToObj(BigDecimal::valueOf)
      .findFirst()
      .orElse(BigDecimal.ZERO);
  }
}
