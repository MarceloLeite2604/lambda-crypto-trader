package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.symbols.GetSymbolsResponsePayload;
import com.github.marceloleite2604.cryptotrader.model.Instrument;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class GetSymbolsResponsePayloadToInstrumentSetMapper
  implements Mapper<GetSymbolsResponsePayload, Set<Instrument>> {

  @Override
  public Set<Instrument> mapTo(GetSymbolsResponsePayload getSymbolsResponsePayload) {

    if (getSymbolsResponsePayload == null) {
      return Collections.emptySet();
    }

    if (CollectionUtils.isEmpty(getSymbolsResponsePayload.getSymbol())) {
      return Collections.emptySet();
    }

    Set<Instrument> instruments = new HashSet<>();

    for (int count = 0; count < getSymbolsResponsePayload.getSymbol()
      .size(); count++) {
      final var instrument = createInstrument(getSymbolsResponsePayload, count);
      instruments.add(instrument);
    }

    return instruments;
  }

  private Instrument createInstrument(GetSymbolsResponsePayload getSymbolsResponsePayload, int count) {
    final var baseCurrency = getSymbolsResponsePayload.getBaseCurrency()
      .get(count);
    final var currency = getSymbolsResponsePayload.getCurrency()
      .get(count);
    final var description = getSymbolsResponsePayload.getDescription()
      .get(count);
    final var exchangeListed = getSymbolsResponsePayload.getExchangeListed()
      .get(count);
    final var exchangeTraded = getSymbolsResponsePayload.getExchangeTraded()
      .get(count);


    final var minMovement = Optional.ofNullable(getSymbolsResponsePayload.getMinMovement()
        .get(count))
      .map(BigDecimal::valueOf)
      .orElse(null);
    final var priceScale = getSymbolsResponsePayload.getPriceScale()
      .get(count);
    final var sessionRegular = getSymbolsResponsePayload.getSessionRegular()
      .get(count);
    final var symbol = getSymbolsResponsePayload.getSymbol()
      .get(count);
    final var timezone = TimeZone.getTimeZone(getSymbolsResponsePayload.getTimezone()
      .get(count));
    final var type = getSymbolsResponsePayload.getType()
      .get(count);

    return Instrument.builder()
      .baseCurrency(baseCurrency)
      .currency(currency)
      .description(description)
      .exchangeListed(exchangeListed)
      .exchangeTraded(exchangeTraded)
      .minMovement(minMovement)
      .priceScale(priceScale)
      .sessionRegular(sessionRegular)
      .symbol(symbol)
      .timezone(timezone)
      .type(type)
      .build();
  }
}
