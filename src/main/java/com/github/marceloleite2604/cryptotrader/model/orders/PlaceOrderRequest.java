package com.github.marceloleite2604.cryptotrader.model.orders;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaceOrderRequest {

  @NotBlank
  private final String accountId;

  @NotBlank
  private final String symbol;

  @NotNull
  private final Boolean async;

  @Positive
  private final BigDecimal cost;

  @Positive
  private final BigDecimal limitPrice;

  @Positive
  private final BigDecimal quantity;

  @NotBlank
  private final String side;

  @NotBlank
  private final String type;
}
