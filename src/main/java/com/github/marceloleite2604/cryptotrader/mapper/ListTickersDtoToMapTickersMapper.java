package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.tickers.TickerDto;
import com.github.marceloleite2604.cryptotrader.model.Ticker;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ListTickersDtoToMapTickersMapper
  implements Mapper<List<TickerDto>, Map<String, Ticker>> {

  private final DateTimeUtil dateTimeUtil;

  @Override
  public Map<String, Ticker> mapTo(List<TickerDto> tickerDtos) {
    if (CollectionUtils.isEmpty(tickerDtos)) {
      return Collections.emptyMap();
    }

    Map<String, Ticker> tickers = new HashMap<>(tickerDtos.size());

    for (TickerDto tickerDto : tickerDtos) {

      final var buy = new BigDecimal(tickerDto.getBuy());
      final var high = new BigDecimal(tickerDto.getHigh());
      final var last = new BigDecimal(tickerDto.getLast());
      final var low = new BigDecimal(tickerDto.getLow());
      final var open = new BigDecimal(tickerDto.getOpen());
      final var sell = new BigDecimal(tickerDto.getSell());
      final var volume = new BigDecimal(tickerDto.getVol());
      final var date = dateTimeUtil.convertTimestampWithNanosToUtcOffsetDateTime(tickerDto.getDate());

      final var ticker = Ticker.builder()
        .buy(buy)
        .high(high)
        .last(last)
        .low(low)
        .open(open)
        .pair(tickerDto.getPair())
        .sell(sell)
        .volume(volume)
        .date(date)
        .build();
      tickers.put(tickerDto.getPair(), ticker);
    }

    return tickers;
  }
}
