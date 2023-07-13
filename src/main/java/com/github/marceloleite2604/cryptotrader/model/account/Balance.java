package com.github.marceloleite2604.cryptotrader.model.account;

import com.github.marceloleite2604.cryptotrader.configuration.GeneralConfiguration;
import com.github.marceloleite2604.cryptotrader.model.Active;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Balance {

  private BigDecimal available;

  private Active active;

  private BigDecimal total;

  public boolean isEmpty() {
    return total.compareTo(GeneralConfiguration.LOGICAL_ZERO) < 0;
  }
}
