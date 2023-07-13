package com.github.marceloleite2604.cryptotrader.model.candles;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CandleType {
  NO_OPERATION(false, false, false, null),
  COMMON(true, true, true, null),
  RISING(false, false, true, true),
  FALLING(false, false, true, false),
  DOJI(true, true, false, null),
  GRAVESTONE(true, false, false, null),
  DRAGONFLY(false, true, false, null),
  SHOOTING_STAR(true, false, true, null),
  HAMMER(false, true, true, null);

  private final boolean upperWickPresent;

  private final boolean lowerWickPresent;

  private final Boolean bodyPresent;

  private final Boolean ascending;

  private boolean match(boolean upperWickPresent, boolean lowerWickPresent, boolean bodyPresent, boolean ascending) {
    final var bodyPresentResult = Optional.ofNullable(this.bodyPresent)
      .map(bp -> bp == bodyPresent)
      .orElse(true);

    final var ascendingResult = Optional.ofNullable(this.ascending)
      .map(asc -> asc == ascending)
      .orElse(true);

    return this.upperWickPresent == upperWickPresent &&
      this.lowerWickPresent == lowerWickPresent &&
      bodyPresentResult &&
      ascendingResult;
  }

  public static CandleType find(boolean upperWickPresent, boolean lowerWickPresent, boolean bodyPresent, boolean ascending) {
    for (CandleType candleType : values()) {
      if (candleType.match(upperWickPresent, lowerWickPresent, bodyPresent, ascending)) {
        return candleType;
      }
    }

    throw new IllegalArgumentException("Unrecognized candle type.");
  }
}
