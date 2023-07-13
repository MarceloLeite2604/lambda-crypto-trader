package com.github.marceloleite2604.cryptotrader.model.candles;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class CandleComparison {

  private final CandlePosition position;

  private final CandleProportion candleProportion;

  private final CandleProportion bodyProportion;

  private final CandleProportion volumeProportion;
}
