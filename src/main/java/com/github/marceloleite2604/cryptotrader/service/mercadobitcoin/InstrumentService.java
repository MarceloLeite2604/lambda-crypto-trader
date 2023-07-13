package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.symbols.GetSymbolsResponsePayload;
import com.github.marceloleite2604.cryptotrader.mapper.GetSymbolsResponsePayloadToInstrumentSetMapper;
import com.github.marceloleite2604.cryptotrader.model.Instrument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Component
@RequiredArgsConstructor
class InstrumentService {

  private final WebClient mbAuthenticatedWebClient;

  private final GetSymbolsResponsePayloadToInstrumentSetMapper getSymbolsResponsePayloadToInstrumentSetMapper;

  public Set<Instrument> retrieveAll() {
    final var symbolResponsePayload = mbAuthenticatedWebClient.get()
      .uri("symbols")
      .retrieve()
      .bodyToMono(GetSymbolsResponsePayload.class)
      .blockOptional()
      .orElseThrow(() -> new IllegalStateException("Could not retrieve all instruments."));

    return getSymbolsResponsePayloadToInstrumentSetMapper.mapTo(symbolResponsePayload);
  }
}
