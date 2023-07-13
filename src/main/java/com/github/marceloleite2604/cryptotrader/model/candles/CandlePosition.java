package com.github.marceloleite2604.cryptotrader.model.candles;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CandlePosition {
  RAISED(v -> v > 0),
  LOWERED(v -> v < 0),
  SAME_LEVEL(v -> v == 0);

  private final Function<Integer, Boolean> comparisonResultMatcher;

  public static CandlePosition findByComparisonResult(final int comparisonValue) {

    return Arrays.stream(CandlePosition.values())
      .filter(candlePosition -> candlePosition.comparisonResultMatcher.apply(comparisonValue))
      .findFirst()
      .orElseThrow(
        () -> {
          final var message = String.format(
            "Could not find candle position for comparison value %d.",
            comparisonValue);
          return new IllegalStateException(message);
        });
  }
}
