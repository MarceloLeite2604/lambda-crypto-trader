package com.github.marceloleite2604.cryptotrader.model.orderbook;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderBookItem {

  private final BigDecimal price;

  private final BigDecimal volume;
}
