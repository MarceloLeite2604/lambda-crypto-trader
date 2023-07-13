package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.tickers.TickerDto;
import com.github.marceloleite2604.cryptotrader.mapper.ListTickersDtoToMapTickersMapper;
import com.github.marceloleite2604.cryptotrader.model.Ticker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
class TickersService {

  private final WebClient mbAuthenticatedWebClient;

  private final ListTickersDtoToMapTickersMapper listTickersDtoToMapTickersMapper;

  public Map<String, Ticker> retrieve(String... symbols) {
    final var tickersUri = buildTickersUri(symbols);
    final var tickerDtos = mbAuthenticatedWebClient.get()
      .uri(tickersUri)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<List<TickerDto>>() {
      })
      .blockOptional()
      .orElseThrow(() -> {
        final var message = String.format("Could not retrieve tickers for \"%s\" symbol(s).", String.join(", ", symbols));
        return new IllegalStateException(message);
      });

    return listTickersDtoToMapTickersMapper.mapTo(tickerDtos);
  }

  @SneakyThrows
  private String buildTickersUri(String... symbols) {
    if (ArrayUtils.isEmpty(symbols)) {
      throw new IllegalArgumentException("Must inform a symbol to retrieve its tickers.");
    }

    final var queryParameterSymbolsNameValuePair = Stream.of(symbols)
      .map(symbol -> (NameValuePair) new BasicNameValuePair("symbols", symbol))
      .toList();

    return new URIBuilder().setPathSegments("tickers")
      .addParameters(queryParameterSymbolsNameValuePair)
      .build()
      .toString();
  }
}
