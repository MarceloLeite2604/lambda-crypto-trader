package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.Instrument;
import org.springframework.stereotype.Component;

@Component
public class InstrumentToActiveMapper implements Mapper<Instrument, Active> {

  @Override
  public Active mapTo(Instrument instrument) {
    return Active.builder()
      .base(instrument.getBaseCurrency())
      .name(instrument.getDescription())
      .quote(instrument.getCurrency())
      .build();
  }
}
