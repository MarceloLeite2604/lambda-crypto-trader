package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.orderbook.GetOrderBookResponsePayload;
import com.github.marceloleite2604.cryptotrader.mapper.GetOrderBookResponsePayloadToOrderBookMapper;
import com.github.marceloleite2604.cryptotrader.model.orderbook.OrderBook;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
class OrderBookService {

  private final WebClient mbAuthenticatedWebClient;

  private final GetOrderBookResponsePayloadToOrderBookMapper getOrderBookResponsePayloadToOrderBookMapper;

  public OrderBook retrieve(String symbol, Integer limit) {
    final var orderBookUri = buildOrderBookUri(symbol, limit);

    final var orderBookResponsePayload = mbAuthenticatedWebClient.get()
      .uri(orderBookUri)
      .retrieve()
      .bodyToMono(GetOrderBookResponsePayload.class)
      .blockOptional()
      .orElseThrow(() -> {
        final var message = String.format("Could not retrieve order book for \"%s\" symbol.", symbol);
        return new IllegalStateException(message);
      });

    return getOrderBookResponsePayloadToOrderBookMapper.mapTo(orderBookResponsePayload);
  }

  @SneakyThrows
  private String buildOrderBookUri(String symbol, Integer limit) {
    final var orderBookUriBuilder = new URIBuilder().setPathSegments(symbol, "orderbook");

    if (limit != null) {
      orderBookUriBuilder.addParameter("limit", limit.toString());
    }

    return orderBookUriBuilder.build()
      .toString();
  }
}
