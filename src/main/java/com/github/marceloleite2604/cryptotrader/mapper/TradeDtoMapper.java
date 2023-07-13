package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.trades.TradeDto;
import com.github.marceloleite2604.cryptotrader.model.trades.Trade;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TradeDtoMapper implements Mapper<TradeDto, Trade> {

  private final DateTimeUtil dateTimeUtil;

  @Override
  public Trade mapTo(TradeDto tradeDto) {

    final var amount = BigDecimal.valueOf(tradeDto.getAmount());

    final var date = dateTimeUtil.convertEpochToUtcOffsetDateTime(tradeDto.getDate());

    final var price = BigDecimal.valueOf(tradeDto.getPrice());

    return Trade.builder()
      .amount(amount)
      .date(date)
      .price(price)
      .tid(tradeDto.getTid())
      .type(tradeDto.getType())
      .build();


  }
}
