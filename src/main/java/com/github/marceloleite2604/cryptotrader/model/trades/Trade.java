package com.github.marceloleite2604.cryptotrader.model.trades;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Trade implements Comparable<Trade> {

  private final BigDecimal amount;

  private final OffsetDateTime date;

  private final BigDecimal price;

  private final long tid;

  private final String type;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Trade trade = (Trade) o;
    return Objects.equals(date, trade.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date);
  }

  @Override
  public int compareTo(Trade other) {
    return date.compareTo(other.date);
  }
}
