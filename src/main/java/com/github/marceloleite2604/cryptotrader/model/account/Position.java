package com.github.marceloleite2604.cryptotrader.model.account;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Position {

  private final BigDecimal averagePrice;

  private final String id;

  private final String instrument;

  private final BigDecimal quantity;

  private final String side;
}
