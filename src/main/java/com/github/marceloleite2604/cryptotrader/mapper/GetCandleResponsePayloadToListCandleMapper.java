package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.candle.GetCandleResponsePayload;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCandleResponsePayloadToListCandleMapper implements Mapper<GetCandleResponsePayload, List<Candle>> {

  private final DateTimeUtil dateTimeUtil;

  @Override
  public List<Candle> mapTo(GetCandleResponsePayload getCandleResponsePayload) {
    if (CollectionUtils.isEmpty(getCandleResponsePayload.getClose())) {
      return Collections.emptyList();
    }

    final var candles = new ArrayList<Candle>(getCandleResponsePayload.getClose()
      .size());

    for (int count = 0; count < getCandleResponsePayload.getClose()
      .size(); count++) {

      final var close = BigDecimal.valueOf(getCandleResponsePayload.getClose()
        .get(count));

      final var high = BigDecimal.valueOf(getCandleResponsePayload.getHigh()
        .get(count));

      final var low = BigDecimal.valueOf(getCandleResponsePayload.getLow()
        .get(count));

      final var open = BigDecimal.valueOf(getCandleResponsePayload.getOpen()
        .get(count));

      final var timestamp = dateTimeUtil.convertEpochToUtcOffsetDateTime(getCandleResponsePayload.getTimestamp()
        .get(count));

      final var volume = BigDecimal.valueOf(getCandleResponsePayload.getVolume()
        .get(count));

      final var candle = Candle.builder()
        .close(close)
        .high(high)
        .low(low)
        .open(open)
        .timestamp(timestamp)
        .volume(volume)
        .build();

      candles.add(candle);
    }

    return candles;
  }
}
