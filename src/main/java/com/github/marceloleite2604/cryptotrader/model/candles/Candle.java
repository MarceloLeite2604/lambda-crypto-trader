package com.github.marceloleite2604.cryptotrader.model.candles;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Candle implements Comparable<Candle> {

  @EqualsAndHashCode.Include
  private final OffsetDateTime timestamp;

  private final BigDecimal open;

  private final BigDecimal high;

  private final BigDecimal low;

  private final BigDecimal close;

  private final CandlePrecision precision;

  private final String symbol;

  private final BigDecimal volume;

  private final BigDecimal size;

  private final BigDecimal bodySize;

  private final BigDecimal upperWickSize;

  private final BigDecimal lowerWickSize;

  private final BigDecimal upperWickPercentage;

  private final BigDecimal lowerWickPercentage;

  private final BigDecimal bodyPercentage;

  private final BigDecimal average;

  private final BigDecimal bodyAverage;

  private final CandleDirection direction;

  private final CandleType type;

  private final CandleComparison comparison;

  @Override
  public int compareTo(Candle other) {
    return timestamp.compareTo(other.timestamp);
  }
}
