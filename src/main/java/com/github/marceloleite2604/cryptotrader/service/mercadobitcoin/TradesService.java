package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.trades.TradeDto;
import com.github.marceloleite2604.cryptotrader.mapper.TradeDtoMapper;
import com.github.marceloleite2604.cryptotrader.model.trades.Trade;
import com.github.marceloleite2604.cryptotrader.model.trades.TradesRequest;
import com.github.marceloleite2604.cryptotrader.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
class TradesService {

  private final WebClient mbAuthenticatedWebClient;

  private final TradeDtoMapper tradeDtoMapper;

  private final ValidationUtil validationUtil;

  public List<Trade> retrieve(TradesRequest tradesRequest) {
    Assert.notNull(tradesRequest, "Must inform a valid trades request.");
    validationUtil.throwIllegalArgumentExceptionIfNotValid(tradesRequest, "Trades request informed is invalid.");

    final var getTradesUri = buildRetrieveUri(tradesRequest);

    final var tradeDtos = mbAuthenticatedWebClient.get()
      .uri(getTradesUri)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<List<TradeDto>>() {
      })
      .blockOptional()
      .orElseThrow(() -> {
        final var message = String.format("Could not retrieve trades for \"%s\" symbol.", tradesRequest.getSymbol());
        return new IllegalStateException(message);
      });

    return tradeDtoMapper.mapAllTo(tradeDtos);
  }

  @SneakyThrows
  private String buildRetrieveUri(TradesRequest tradesRequest) {

    final var uriBuilder = new URIBuilder().setPathSegments(tradesRequest.getSymbol(), "trades");

    if (tradesRequest.getTid() != null) {
      uriBuilder.addParameter("tid", Long.toString(tradesRequest.getTid()));
    }

    if (tradesRequest.getSince() != null) {
      uriBuilder.addParameter("since", Long.toString(tradesRequest.getSince()));
    }

    if (tradesRequest.getFrom() != null) {
      uriBuilder.addParameter("from", Long.toString(tradesRequest.getFrom()
        .toEpochSecond()));
    }

    if (tradesRequest.getTo() != null) {
      uriBuilder.addParameter("to", Long.toString(tradesRequest.getTo()
        .toEpochSecond() - 1));
    }

    return uriBuilder
      .build()
      .toString();
  }
}
