package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.orders.OrderDto;
import com.github.marceloleite2604.cryptotrader.model.orders.Order;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderDtoMapper implements Mapper<OrderDto, Order> {

  private final DateTimeUtil dateTimeUtil;

  private final ExecutionDtoMapper executionDtoMapper;

  @Override
  public Order mapTo(OrderDto orderDto) {

    final var executions = executionDtoMapper.mapAllTo(orderDto.getExecutions());

    final var averagePrice = BigDecimal.valueOf(orderDto.getAvgPrice());

    final var limitPrice = BigDecimal.valueOf(orderDto.getLimitPrice());

    final var filledQuantity = BigDecimal.valueOf(orderDto.getFilledQty());

    final var quantity = BigDecimal.valueOf(orderDto.getQty());

    final var createdAt = dateTimeUtil.convertEpochToUtcOffsetDateTime(orderDto.getCreatedAt());

    final var updatedAt = dateTimeUtil.convertEpochToUtcOffsetDateTime(orderDto.getUpdatedAt());

    final var fee = BigDecimal.valueOf(orderDto.getFee());

    final var triggerOrderId = orderDto.getTriggerOrderId();


    return Order.builder()
      .averagePrice(averagePrice)
      .createdAt(createdAt)
      .id(orderDto.getId())
      .executions(executions)
      .instrument(orderDto.getInstrument())
      .filledQuantity(filledQuantity)
      .limitPrice(limitPrice)
      .quantity(quantity)
      .side(orderDto.getSide())
      .status(orderDto.getStatus())
      .type(orderDto.getType())
      .updatedAt(updatedAt)
      .fee(fee)
      .triggerOrderId(triggerOrderId)
      .build();

  }
}
