package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.account.BalanceDto;
import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.account.Balance;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceDtoMapper implements Mapper<BalanceDto, Balance> {

  @Override
  public Balance mapTo(BalanceDto balanceDto) {

    final var available = BigDecimal.valueOf(balanceDto.getAvailable());
    final var total = BigDecimal.valueOf(balanceDto.getTotal());

    /* API returns base on symbol property. */
    final var active = Active.findByBase(balanceDto.getSymbol());

    return Balance.builder()
      .available(available)
      .active(active)
      .total(total)
      .build();
  }
}
