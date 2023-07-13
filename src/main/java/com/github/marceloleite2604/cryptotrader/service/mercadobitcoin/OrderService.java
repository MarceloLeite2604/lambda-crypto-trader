package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.orders.OrderDto;
import com.github.marceloleite2604.cryptotrader.dto.orders.PostOrderResponsePayload;
import com.github.marceloleite2604.cryptotrader.mapper.OrderDtoMapper;
import com.github.marceloleite2604.cryptotrader.mapper.PlaceOrderRequestToPostOrderRequestPayloadMapper;
import com.github.marceloleite2604.cryptotrader.model.orders.Order;
import com.github.marceloleite2604.cryptotrader.model.orders.PlaceOrderRequest;
import com.github.marceloleite2604.cryptotrader.model.orders.RetrieveOrdersRequest;
import com.github.marceloleite2604.cryptotrader.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.Asserts;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderService {

  public static final String ACCOUNTS = "accounts";
  public static final String ORDERS = "orders";
  private final WebClient mbAuthenticatedWebClient;

  private final ValidationUtil validationUtil;

  private final OrderDtoMapper orderDtoMapper;

  private final PlaceOrderRequestToPostOrderRequestPayloadMapper placeOrderRequestToPostOrderRequestPayloadMapper;

  public List<Order> retrieve(RetrieveOrdersRequest retrieveOrdersRequest) {
    Assert.notNull(retrieveOrdersRequest, "Must inform a valid orders request");
    validationUtil.throwIllegalArgumentExceptionIfNotValid(retrieveOrdersRequest, "Orders request informed is invalid.");

    final var getOrdersUri = buildGetOrdersUri(retrieveOrdersRequest);

    final var orderDtos = mbAuthenticatedWebClient.get()
      .uri(getOrdersUri)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<List<OrderDto>>() {
      })
      .blockOptional()
      .orElseThrow(() -> {
        final var message = String.format("Could not retrieve orders for \"%s\" symbol.", retrieveOrdersRequest.getSymbol());
        return new IllegalStateException(message);
      });

    return orderDtoMapper.mapAllTo(orderDtos);
  }

  @SneakyThrows
  private String buildGetOrdersUri(RetrieveOrdersRequest retrieveOrdersRequest) {
    final var uriBuilder = new URIBuilder().setPathSegments(ACCOUNTS, retrieveOrdersRequest.getAccountId(), retrieveOrdersRequest.getSymbol(), ORDERS);

    if (retrieveOrdersRequest.getHasExecutions() != null) {
      uriBuilder.addParameter("has_executions", retrieveOrdersRequest.getHasExecutions()
        .toString());
    }

    if (retrieveOrdersRequest.getType() != null) {
      uriBuilder.addParameter("type", retrieveOrdersRequest.getType());
    }

    if (retrieveOrdersRequest.getStatus() != null) {
      uriBuilder.addParameter("status", retrieveOrdersRequest.getStatus());
    }

    if (retrieveOrdersRequest.getIdFrom() != null) {
      uriBuilder.addParameter("id_from", retrieveOrdersRequest.getIdFrom());
    }

    if (retrieveOrdersRequest.getIdTo() != null) {
      uriBuilder.addParameter("id_to", retrieveOrdersRequest.getIdTo());
    }

    if (retrieveOrdersRequest.getCreatedAtFrom() != null) {
      uriBuilder.addParameter("created_at_from", Long.toString(retrieveOrdersRequest.getCreatedAtFrom()
        .toEpochSecond()));
    }

    if (retrieveOrdersRequest.getCreatedAtTo() != null) {
      uriBuilder.addParameter("created_at_to", Long.toString(retrieveOrdersRequest.getCreatedAtTo()
        .toEpochSecond()));
    }

    return uriBuilder
      .build()
      .toString();
  }

  public Order retrieveOrder(String accountId, String symbol, String orderId) {
    Asserts.notNull(accountId, "Account ID cannot be null.");
    Asserts.notNull(symbol, "Symbol cannot be null.");
    Asserts.notNull(orderId, "Order ID cannot be null.");

    final var getOrdersUri = buildGetOrderUri(accountId, symbol, orderId);

    final var orderDto = mbAuthenticatedWebClient.get()
      .uri(getOrdersUri)
      .retrieve()
      .bodyToMono(OrderDto.class)
      .blockOptional()
      .orElseThrow(() -> {
        final var message = String.format("Could not retrieve order \"%s\".", orderId);
        return new IllegalStateException(message);
      });

    return orderDtoMapper.mapTo(orderDto);
  }

  @SneakyThrows
  private String buildGetOrderUri(String accountId, String symbol, String orderId) {
    return new URIBuilder().setPathSegments(ACCOUNTS, accountId, symbol, ORDERS, orderId)
      .build()
      .toString();
  }

  public Optional<String> placeOrder(PlaceOrderRequest placeOrderRequest) {
    Assert.notNull(placeOrderRequest, "Order creation request cannot be null.");
    validationUtil.throwIllegalArgumentExceptionIfNotValid(placeOrderRequest, "Invalid order creation request.");
    final var postOrderUri = buildPostUri(placeOrderRequest.getAccountId(), placeOrderRequest.getSymbol());

    final var postOrderRequestPayload = placeOrderRequestToPostOrderRequestPayloadMapper.mapTo(placeOrderRequest);

    return mbAuthenticatedWebClient.post()
      .uri(postOrderUri)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(postOrderRequestPayload))
      .retrieve()
      .bodyToMono(PostOrderResponsePayload.class)
      .onErrorResume(WebClientResponseException.class,
        webClientResponseException -> {
          final var message = String.format(
            "Received HTTP status \"%s\" while creating order and the following message: %s",
            webClientResponseException.getStatusCode(),
            webClientResponseException.getResponseBodyAsString());
          log.warn(message);
          return Mono.empty();
        })
      .blockOptional()
      .map(PostOrderResponsePayload::getOrderId);
  }

  @SneakyThrows
  private String buildPostUri(String accountId, String symbol) {
    return new URIBuilder().setPathSegments(ACCOUNTS, accountId, symbol, ORDERS)
      .build()
      .toString();

  }

  public boolean cancelOrder(String accountId, String symbol, String orderId) {
    Asserts.notNull(accountId, "Account ID cannot be null.");
    Asserts.notNull(symbol, "Symbol cannot be null.");
    Asserts.notNull(orderId, "Order ID cannot be null.");

    final var deleteOrderUri = buildDeleteOrderUri(accountId, symbol, orderId);

    final var result = mbAuthenticatedWebClient.delete()
      .uri(deleteOrderUri)
      .retrieve()
      .toBodilessEntity()
      .map(voidResponseEntity -> true)
      .onErrorResume(WebClientResponseException.class,
        webClientResponseException -> {
          final var message = String.format(
            "Received HTTP status \"%s\" while deleting order and the following message: %s",
            webClientResponseException.getStatusCode(),
            webClientResponseException.getResponseBodyAsString());
          log.warn(message);
          return Mono.justOrEmpty(false);
        })
      .block();

    return Boolean.TRUE.equals(result);
  }

  private String buildDeleteOrderUri(String accountId, String symbol, String orderId) {
    return buildGetOrderUri(accountId, symbol, orderId);
  }
}
