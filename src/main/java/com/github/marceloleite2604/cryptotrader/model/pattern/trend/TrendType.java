package com.github.marceloleite2604.cryptotrader.model.pattern.trend;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Function;

@RequiredArgsConstructor
public enum TrendType {
  UNDEFINED(v -> v == 0),
  DOWNTREND(v -> v < 0),
  UPTREND(v -> v > 0);

  private final Function<Integer, Boolean> comparisonResultMatcher;

  public static TrendType findByComparisonResult(final int comparisonValue) {

    return Arrays.stream(TrendType.values())
      .filter(trendType -> trendType.comparisonResultMatcher.apply(comparisonValue))
      .findFirst()
      .orElseThrow(
        () -> {
          final var message = String.format(
            "Could not find trend type for comparison value %d.",
            comparisonValue);
          return new IllegalStateException(message);
        });
  }
}
