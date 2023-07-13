package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.account.PositionDto;
import com.github.marceloleite2604.cryptotrader.model.account.Position;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PositionDtoMapper implements Mapper<PositionDto, Position> {

  @Override
  public Position mapTo(PositionDto positionDto) {

    final var averagePrice = BigDecimal.valueOf(positionDto.getAvgPrice());
    final var quantity = BigDecimal.valueOf(positionDto.getQuantity());

    return Position.builder()
      .id(positionDto.getId())
      .instrument(positionDto.getInstrument())
      .side(positionDto.getSide())
      .averagePrice(averagePrice)
      .quantity(quantity)
      .build();
  }
}
