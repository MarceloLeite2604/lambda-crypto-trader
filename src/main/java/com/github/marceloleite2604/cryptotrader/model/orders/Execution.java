package com.github.marceloleite2604.cryptotrader.model.orders;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Execution {

  private final OffsetDateTime executedAt;

  private final String id;

  private final String instrument;

  private final BigDecimal price;

  private final BigDecimal quantity;

  private final String side;
}
