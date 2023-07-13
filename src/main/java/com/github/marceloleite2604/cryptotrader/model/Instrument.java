package com.github.marceloleite2604.cryptotrader.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.TimeZone;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@Getter
public class Instrument {

  private final String baseCurrency;

  private final String currency;

  private final String description;

  private final boolean exchangeListed;

  private final boolean exchangeTraded;

  private final BigDecimal minMovement;

  private final long priceScale;

  private final String sessionRegular;

  @EqualsAndHashCode.Include
  private final String symbol;

  private final TimeZone timezone;

  private final String type;
}
