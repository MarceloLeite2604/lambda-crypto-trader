package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.orders.PostOrderRequestPayload;
import com.github.marceloleite2604.cryptotrader.model.orders.PlaceOrderRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class PlaceOrderRequestToPostOrderRequestPayloadMapper
  implements Mapper<PlaceOrderRequest, PostOrderRequestPayload> {

  @Override
  public PostOrderRequestPayload mapTo(PlaceOrderRequest placeOrderRequest) {

    final var cost = Optional.ofNullable(placeOrderRequest.getCost())
      .map(BigDecimal::doubleValue)
      .orElse(null);

    final var qty = Optional.ofNullable(placeOrderRequest.getQuantity())
      .map(BigDecimal::doubleValue)
      .orElse(null);

    final var limitPrice = Optional.ofNullable(placeOrderRequest.getLimitPrice())
      .map(BigDecimal::doubleValue)
      .orElse(null);

    return PostOrderRequestPayload.builder()
      .async(placeOrderRequest.getAsync())
      .cost(cost)
      .qty(qty)
      .limitPrice(limitPrice)
      .side(placeOrderRequest.getSide())
      .type(placeOrderRequest.getType())
      .build();
  }
}
