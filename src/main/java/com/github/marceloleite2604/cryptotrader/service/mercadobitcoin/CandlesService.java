package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.candle.GetCandleResponsePayload;
import com.github.marceloleite2604.cryptotrader.mapper.GetCandleResponsePayloadToListCandleMapper;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlesRequest;
import com.github.marceloleite2604.cryptotrader.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class CandlesService {

  private final WebClient mbAuthenticatedWebClient;

  private final ValidationUtil validationUtil;

  private final GetCandleResponsePayloadToListCandleMapper getCandleResponsePayloadToListCandleMapper;

  public List<Candle> retrieve(CandlesRequest candlesRequest) {

    Assert.notNull(candlesRequest, "Must inform a valid candle request.");
    validationUtil.throwIllegalArgumentExceptionIfNotValid(candlesRequest, "Candles request is invalid.");

    final var retrieveCandlesUri = buildRetrieveUri(candlesRequest);

    final var getCandleResponsePayload = mbAuthenticatedWebClient.get()
      .uri(retrieveCandlesUri)
      .retrieve()
      .bodyToMono(GetCandleResponsePayload.class)
      .blockOptional()
      .orElseThrow(() -> {
        final var message = String.format("Could not retrieve candles for \"%s\" symbol.", candlesRequest.getActive());
        return new IllegalStateException(message);
      });

    return getCandleResponsePayloadToListCandleMapper.mapTo(getCandleResponsePayload)
      .stream()
      .map(Candle::toBuilder)
      .map(builder -> builder.precision(candlesRequest.getResolution())
        .symbol(candlesRequest.getActive()
          .getSymbol())
        .build())
      .sorted()
      .collect(Collectors.toCollection(ArrayList::new));
  }

  @SneakyThrows
  private String buildRetrieveUri(CandlesRequest candlesRequest) {

    final var uriBuilder = new URIBuilder().setPathSegments("candles")
      .addParameter("symbol", candlesRequest.getActive()
        .getSymbol())
      .addParameter("resolution", candlesRequest.getResolution()
        .getValue());

    if (candlesRequest.getCountback() != null) {
      uriBuilder.addParameter("to", Integer.toString(candlesRequest.getToCount()));
      uriBuilder.addParameter("countback", Integer.toString(candlesRequest.getCountback()));
    } else {
      uriBuilder.addParameter("to", Long.toString(candlesRequest.getToTime()
        .toEpochSecond() - 1));
      uriBuilder.addParameter("from", Long.toString(candlesRequest.getFrom()
        .toEpochSecond()));
    }

    return uriBuilder
      .build()
      .toString();
  }
}
