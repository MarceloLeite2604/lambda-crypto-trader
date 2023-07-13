package com.github.marceloleite2604.cryptotrader.service;

import com.github.marceloleite2604.cryptotrader.mapper.InstrumentToActiveMapper;
import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.service.mercadobitcoin.MercadoBitcoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActiveService {

  private final MercadoBitcoinService mercadoBitcoinService;
  private final InstrumentToActiveMapper instrumentToActiveMapper;

  public void retrieve() {
    mercadoBitcoinService.retrieveAllInstruments()
      .stream()
      .map(instrumentToActiveMapper::mapTo)
      .forEach(Active::add);
  }
}
