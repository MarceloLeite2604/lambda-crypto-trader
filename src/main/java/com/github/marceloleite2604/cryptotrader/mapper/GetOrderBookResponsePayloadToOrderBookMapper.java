package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.orderbook.GetOrderBookResponsePayload;
import com.github.marceloleite2604.cryptotrader.model.orderbook.OrderBook;
import com.github.marceloleite2604.cryptotrader.model.orderbook.OrderBookItem;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOrderBookResponsePayloadToOrderBookMapper
  implements Mapper<GetOrderBookResponsePayload, OrderBook> {

  private final DateTimeUtil dateTimeUtil;

  @Override
  public OrderBook mapTo(GetOrderBookResponsePayload getOrderBookResponsePayload) {

    final var asks = createOrders(getOrderBookResponsePayload.getAsks());
    final var bids = createOrders(getOrderBookResponsePayload.getBids());
    final var timestamp = dateTimeUtil.convertTimestampWithNanosToUtcOffsetDateTime(
      getOrderBookResponsePayload.getTimestamp());

    return OrderBook.builder()
      .asks(asks)
      .bids(bids)
      .timestamp(timestamp)
      .build();
  }

  private List<OrderBookItem> createOrders(List<double[]> rawOrders) {
    if (CollectionUtils.isEmpty(rawOrders)) {
      return Collections.emptyList();
    }

    List<OrderBookItem> orderBookItems = new ArrayList<>(rawOrders.size());

    for (double[] rawOrder : rawOrders) {
      final var price = BigDecimal.valueOf(rawOrder[0]);
      final var volume = BigDecimal.valueOf(rawOrder[1]);
      final var order = OrderBookItem.builder()
        .price(price)
        .volume(volume)
        .build();

      orderBookItems.add(order);
    }

    return orderBookItems;
  }
}
