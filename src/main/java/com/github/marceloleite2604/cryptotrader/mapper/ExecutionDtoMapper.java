package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.orders.ExecutionDto;
import com.github.marceloleite2604.cryptotrader.model.orders.Execution;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ExecutionDtoMapper implements Mapper<ExecutionDto, Execution> {

  private final DateTimeUtil dateTimeUtil;

  @Override
  public Execution mapTo(ExecutionDto executionDto) {

    final var executedAt = dateTimeUtil.convertEpochToUtcOffsetDateTime(executionDto.getExecutedAt());

    final var quantity = BigDecimal.valueOf(executionDto.getQty());

    final var price = BigDecimal.valueOf(executionDto.getPrice());

    return Execution.builder()
      .executedAt(executedAt)
      .quantity(quantity)
      .id(executionDto.getId())
      .instrument(executionDto.getInstrument())
      .price(price)
      .side(executionDto.getSide())
      .build();
  }
}
