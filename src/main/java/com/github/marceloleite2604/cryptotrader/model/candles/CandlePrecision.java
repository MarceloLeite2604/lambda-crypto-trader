package com.github.marceloleite2604.cryptotrader.model.candles;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CandlePrecision {

  ONE_MINUTE("1m", "one minute", Duration.ofMinutes(1), false),
  THREE_MINUTES("3m", "three minutes", Duration.ofMinutes(3), false),
  FIVE_MINUTES("5m", "five minutes", Duration.ofMinutes(5), false),
  FIFTEEN_MINUTES("15m", "fifteen minutes", Duration.ofMinutes(15), true),
  THIRTY_MINUTES("30m", "thirty minutes", Duration.ofMinutes(30), true),
  ONE_HOUR("1h", "one hour", Duration.ofHours(1), true),
  ONE_DAY("1d", "one day", Duration.ofDays(1), true),
  ONE_WEEK("1w", "one week", Duration.ofDays(7), true),
  ONE_MONTH("1M", "one month", Duration.ofDays(30), true);

  private final String value;

  private final String description;

  private final Duration duration;

  private final boolean retrievableFromMb;

  public static CandlePrecision findByValue(String value) {
    if (value == null) {
      return null;
    }

    return Stream.of(values())
      .filter(active -> active.getValue()
        .equals(value))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException(String.format("Could not find candle precision for value \"%s\".", value)));
  }
}
